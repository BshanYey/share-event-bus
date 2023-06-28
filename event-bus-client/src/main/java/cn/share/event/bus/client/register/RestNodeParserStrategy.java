package cn.share.event.bus.client.register;

import cn.share.event.bus.client.annotation.EventRegister;
import cn.share.event.bus.client.entity.param.GenericParam;
import cn.share.event.bus.client.entity.param.Node;
import cn.share.event.bus.client.entity.param.Param;
import cn.share.event.bus.client.entity.param.RestNode;
import cn.share.event.bus.client.entity.parser.RestParseNode;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-27 13:59:06
 * @describe class responsibility
 */
public class RestNodeParserStrategy extends NodeParserStrategy<RestParseNode> {

    private static final String LEFT_LABEL = "{";

    private static final String RIGHT_LABEL = "}";

    private static final String PATH_SUFFIX = "/";

    @Override
    public Node parse(RestParseNode parseNode) {
        EventRegister eventRegister = parseNode.getEventRegister();

        RestNode restNode = new RestNode();

        fullNodeByAnnotation(restNode, eventRegister);

        parseInput(restNode, parseNode.getMethod());

        parseOutput(restNode, parseNode.getMethod());

        return restNode;
    }

    private void parseInput(RestNode restNode, Method method) {

        String methodPath = fullFieldAndGetPath(restNode, method);

        doBuildUri(restNode, methodPath);

        isPathValue(restNode);

        buildMethodParameters(restNode, method);
    }

    private String fullFieldAndGetPath(RestNode restNode, Method method) {
        String methodPath = "";
        if (method.isAnnotationPresent(PostMapping.class)) {
            PostMapping postMapping = method.getAnnotation(PostMapping.class);

            methodPath = postMapping.value()[0];

            restNode.setHttpMethod(String.valueOf(HttpMethod.POST));
            restNode.setWhetherNormal(true);

        } else if (method.isAnnotationPresent(GetMapping.class)) {
            GetMapping getMapping = method.getAnnotation(GetMapping.class);

            methodPath = getMapping.value()[0];

            restNode.setHttpMethod(String.valueOf(HttpMethod.GET));
            restNode.setWhetherNormal(true);

        } else if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            methodPath = requestMapping.value()[0];

            restNode.setHttpMethod(String.valueOf(requestMapping.method()[0]));

            if (StringUtils.containsAny(restNode.getHttpMethod(), String.valueOf(RequestMethod.GET), String.valueOf(RequestMethod.POST))) {
                throw new RuntimeException("暂时不支持 [" + restNode.getHttpMethod() + "] 方式请求.");
            }

            restNode.setWhetherNormal(true);
        } else {
            restNode.setWhetherNormal(false);
        }
        return methodPath;
    }

    private void doBuildUri(RestNode node, String path) {
        if (StringUtils.isNotBlank(node.getUri())) {
            path = node.getUri().concat(PATH_SUFFIX).concat(path);
        }

        node.setUri(path.replaceAll("//", PATH_SUFFIX));
    }

    private void isPathValue(RestNode node) {
        if (StringUtils.contains(node.getUri(), LEFT_LABEL) && StringUtils.contains(node.getUri(), RIGHT_LABEL)) {
            node.setIncludePathValue(true);
        } else {
            node.setIncludePathValue(false);
        }
    }

    private void buildMethodParameters(RestNode node, Method method) {
        Type[] genericParameterTypes = method.getGenericParameterTypes();

        List<Param> params = new ArrayList<>();
        Param param;

        for (int i = 0; i < genericParameterTypes.length; i++) {
            param = new Param();

            Type parameterType = genericParameterTypes[i];
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();

            if (isGenerics(parameterType)) {
                Type rawType = ((ParameterizedType) parameterType).getRawType();
                Class<?> clazz = (Class<?>) rawType;

                // 先填充其他属性
                fullParams(param, i, parameterAnnotations, clazz);

                // 如果是java class ,则需要对泛型类型做校验
                if (isJavaClass(clazz)) {
                    handlerGenerics(param, parameterType, clazz);
                }
            } else {
                Class<?> clazz = (Class<?>) parameterType;
                if (isServletSubClass(clazz)) {
                    continue;
                }
                fullParams(param, i, parameterAnnotations, clazz);

                param.setParamType(clazz.getName());
            }
            params.add(param);
        }

        node.setInput(new Gson().toJson(params));
    }

    private void handlerGenerics(Param param, Type parameterType, Class<?> clazz) {
        // 统一设置泛型类型
        param.setParamType(parameterType.getTypeName());

        if (clazz == List.class || clazz == Set.class) {
            // 泛型类型只有一个
            Type actualTypeArguments = ((ParameterizedType) parameterType).getActualTypeArguments()[0];
            Class<?> paramClazz = (Class<?>) actualTypeArguments;

            if (!isJavaClass(paramClazz)) {
                Map<String, String> paramMap = convertField(paramClazz);
                param.setParamValue(new Gson().toJson(paramMap));
            }

        } else if (clazz == Map.class) {
            // 两个
            Type[] actualTypeArguments = ((ParameterizedType) parameterType).getActualTypeArguments();

            GenericParam firstGeneric = genericParam((Class<?>)actualTypeArguments[0]);
            firstGeneric.setIsKey(Boolean.TRUE);

            GenericParam secondGeneric = genericParam((Class<?>)actualTypeArguments[1]);

            param.setParamValue(new Gson().toJson(Arrays.asList(firstGeneric, secondGeneric)));
        }
    }

    private GenericParam genericParam(Class<?> secondClazz) {
        GenericParam secondGeneric = new GenericParam();
        secondGeneric.setKey(secondClazz.getName());
        if (isJavaClass(secondClazz)) {
            secondGeneric.setIsJavaClass(Boolean.TRUE);
        } else {
            secondGeneric.setIsJavaClass(Boolean.FALSE);

            Map<String, String> paramMap = convertField(secondClazz);
            secondGeneric.setValue(new Gson().toJson(paramMap));
        }
        return secondGeneric;
    }

    private void fullParams(Param param, int index, Annotation[][] parameterAnnotations, Class<?> clazz) {
        if (isJavaClass(clazz) || clazz == MultipartFile.class || clazz == MultipartFile[].class) {
            for (Annotation annotation : parameterAnnotations[index]) {
                // 只需要处理 RequestParam PathVariable 两种注解,其他的不用处理
                if (annotation instanceof RequestParam) {
                    param.setParamName(((RequestParam) annotation).value());
                } else if (annotation instanceof PathVariable) {
                    param.setParamName(((PathVariable) annotation).value());
                }
                param.setJavaClass(true);
            }
        } else {
            // 自定义类型,直接将数据转换为map key -> value
            Map<String, String> paramValueMap = convertField(clazz);
            param.setParamName(clazz.getName());
            param.setParamValue(new Gson().toJson(paramValueMap));
            param.setJavaClass(false);
        }
        param.setParamOrder(index + 1);
    }

    private void parseOutput(RestNode restNode, Method method) {
        restNode.setOutput(new Gson().toJson(super.convertField(method.getReturnType())));
    }
}
