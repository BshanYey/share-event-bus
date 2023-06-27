package cn.share.event.bus.client.register;

import cn.share.event.bus.client.annotation.EventRegister;
import cn.share.event.bus.client.entity.param.GenericParam;
import cn.share.event.bus.client.entity.param.Node;
import cn.share.event.bus.client.entity.param.Param;
import cn.share.event.bus.client.entity.param.RestNode;
import cn.share.event.bus.client.entity.parser.EventParseNode;
import cn.share.event.bus.client.entity.parser.ParseNode;
import cn.share.event.bus.client.entity.parser.RestParseNode;
import cn.share.event.bus.client.enums.RegisterType;
import cn.share.event.bus.client.store.DataStorage;
import com.google.gson.Gson;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-26 11:16:09
 * @describe class responsibility
 */
@Component
public class RegisterNodeProcessor implements InitializingBean {

    private static final String LEFT_LABEL = "{";

    private static final String RIGHT_LABEL = "}";

    private static final String PATH_SUFFIX = "/";

    @Value("server.servlet.context-path:/")
    private String contentPath;


    private final AtomicInteger registerCount = new AtomicInteger(0);

    private final BlockingQueue<Object> registerQueue = new ArrayBlockingQueue<>(1024);


    public void register(Object obj) {
        registerCount.getAndIncrement();
        registerQueue.offer(obj);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CompletableFuture.runAsync(this::doStartRegister);
    }

    private void doStartRegister() {
        while (true) {
            try {
                Object bean = registerQueue.take();
                // 解析
                parseBean(bean);
            } catch (Exception e) {

            }
        }
    }

    private void parseBean(Object take) {
        Class<?> clazz = take.getClass();
        Method[] declaredMethods = clazz.getDeclaredMethods();

        for (Method method : declaredMethods) {
            if (!method.isAnnotationPresent(EventRegister.class)) {
                continue;
            }
            String api = "";
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                api = controllerPath(clazz);
            }
            parseMethod(api, method);
        }
    }

    private String controllerPath(Class<?> clazz) {
        String controllerPath = clazz.getAnnotation(RequestMapping.class).value()[0];
        String uri = "";

        if (StringUtils.isNotBlank(controllerPath)) {
            if (StringUtils.startsWith(controllerPath, PATH_SUFFIX)) {
                uri = contentPath.concat(controllerPath);
            }else {
                uri = contentPath.concat(PATH_SUFFIX).concat(controllerPath);
            }
        }

        if (!StringUtils.startsWith(uri, PATH_SUFFIX)) {
            uri = PATH_SUFFIX.concat(uri);
        }

        return uri.replaceAll("//", PATH_SUFFIX);
    }

    private void parseMethod(String api, Method method) {
        EventRegister eventRegister = method.getAnnotation(EventRegister.class);

        RegisterType registerType = eventRegister.registerType();

        ParseNode parseNode;
        if (registerType == RegisterType.MQ) {
            parseNode = new EventParseNode();
            parseNode.setMethod(method);
            parseNode.setEventRegister(eventRegister);
        } else if (registerType == RegisterType.REST) {
            RestParseNode restParseNode = new RestParseNode();
            restParseNode.setEventRegister(eventRegister);
            restParseNode.setMethod(method);
            restParseNode.setApi(api);
            parseNode = restParseNode;
        } else {
            throw new RuntimeException("暂不支持[" + registerType + "]注册类型");
        }

        // 解析封装节点信息
        Node node = NodeProvider.getNodeParse(registerType).parse(parseNode);

        // 注册
        DataStorage.registerNode(node);

        /*if (RegisterType.MQ == registerType) {
            EventNode eventNode = new EventNode();
            eventNode.setNodeName(eventRegister.funcName());
            eventNode.setTopic(eventRegister.topic());
            eventNode.setDesc(eventRegister.desc());
            eventNode.setInput(null);
        } else {
            RestNode restNode = new RestNode();
            restNode.setNodeName(eventRegister.funcName());
            restNode.setDesc(eventRegister.desc());
            restNode.setUri(api);
            handlerInput(restNode, method);
            handlerOutput(restNode, method);
        }*/
    }

    private void handlerOutput(RestNode restNode, Method method) {
        Type genericReturnType = method.getGenericReturnType();





        Class<?> returnType = method.getReturnType();

        Param param = new Param();
        param.setParamType(returnType.getName());
        if (isJavaClass(returnType)) {


            param.setJavaClass(true);
        } else {
            Map<String, String> fieldMap = convertField(returnType);
            param.setParamValue(new Gson().toJson(fieldMap));
            param.setJavaClass(false);
        }

        restNode.setOutput(new Gson().toJson(param));

    }

    private void handlerInput(RestNode node, Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            node.setWhetherNormal(true);
            doParseRequestMapping(node, method);
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            doParsePostMapping(node, method);
            node.setWhetherNormal(true);
        } else if (method.isAnnotationPresent(GetMapping.class)) {
            doParseGetMapping(node, method);
            node.setWhetherNormal(true);
        } else {
            // 暂时不支持该请求
            node.setWhetherNormal(false);
        }
    }

    private void doParseGetMapping(RestNode node, Method method) {
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        // 判断是否存在 pathValue
        doBuildUri(node, getMapping.value()[0]);

        // 判断是否存在 pathValue
        judgePathValue(node);

        node.setHttpMethod(String.valueOf(HttpMethod.GET));

        // 获取请求参数
        buildReqParam(node, method);
    }

    private void doParsePostMapping(RestNode node, Method method) {
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        // 判断是否存在 pathValue
        doBuildUri(node, postMapping.value()[0]);

        // 判断是否存在 pathValue
        judgePathValue(node);

        node.setHttpMethod(String.valueOf(HttpMethod.POST));

        // 获取请求参数
        buildReqParam(node, method);
    }

    private void doParseRequestMapping(RestNode node, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        // 构建uri
        doBuildUri(node, requestMapping.value()[0]);

        // 判断是否存在 pathValue
        judgePathValue(node);

        // 获取请求类型
        httpRequestType(requestMapping, node);

        // 封装参数
        buildReqParam(node, method);
    }

    private void buildReqParam(RestNode node, Method method) {
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        List<Param> params = new ArrayList<>();
        Param param;

        for (int i = 0; i < genericParameterTypes.length; i++) {
            param = new Param();

            Type parameterType = genericParameterTypes[i];
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();

            if (parameterType instanceof ParameterizedType) {
                Type rawType = ((ParameterizedType) parameterType).getRawType();

                Class<?> clazz = (Class<?>) rawType;
                // 先填充其他属性
                fullParams(param, i, parameterAnnotations, clazz);

                // 如果是java class ,则需要对泛型类型做校验
                if (isJavaClass(clazz)) {
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
                        GenericParam genericParam = new GenericParam();

                        Class<?> firstClazz =  (Class<?>)actualTypeArguments[0];
                        if (isJavaClass(firstClazz)) {
                            genericParam.setKey(firstClazz.getName());
                        } else {
                            Map<String, String> paramMap = convertField(firstClazz);
                            genericParam.setKey(new Gson().toJson(paramMap));
                        }

                        Class<?> secondClazz =  (Class<?>)actualTypeArguments[1];
                        if (isJavaClass(secondClazz)) {
                            genericParam.setValue(secondClazz.getName());
                        } else {
                            Map<String, String> paramMap = convertField(secondClazz);
                            genericParam.setKey(new Gson().toJson(paramMap));
                        }
                        param.setParamValue(new Gson().toJson(genericParam));
                    }
                    // 其他类型忽略
                }
            } else {
                Class<?> clazz = (Class<?>) parameterType;
                // 如果请求参数是httpServletRequest HttpServletResponse 两种,则直接跳过
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

    private Map<String, String> convertField(Class<?> clazz) {
        Map<String, String> result = new HashMap<>(16);

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (isJavaClass(field.getType())) {
                result.put(field.getName(), field.getType().getName());
            } else {
                Map<String, String> fieldMap = convertField(field.getType());
                result.put(field.getName(), new Gson().toJson(fieldMap));
            }
        }

        return result;
    }

    private void httpRequestType(RequestMapping requestMapping, RestNode node) {
        String httpMethod = String.valueOf(requestMapping.method()[0]);
        if (!StringUtils.containsAny(httpMethod, HttpMethod.GET.toString(),  HttpMethod.POST.toString())) {
            // 不支持请求
            node.setWhetherNormal(false);
        } else {
            node.setWhetherNormal(true);
            node.setHttpMethod(httpMethod);
        }
    }

    private void judgePathValue(RestNode node) {
        if (StringUtils.contains(node.getUri(), LEFT_LABEL) && StringUtils.contains(node.getUri(), RIGHT_LABEL)) {
            node.setIncludePathValue(true);
        } else {
            node.setIncludePathValue(false);
        }
    }

    private void doBuildUri(RestNode node, String path) {
        if (StringUtils.isNotBlank(node.getUri())) {
            path = node.getUri().concat(PATH_SUFFIX).concat(path);
        }

        node.setUri(path.replaceAll("//", PATH_SUFFIX));
    }

    private Map<String, String> customerBean(Class<?> parameterType) {
        Map<String, String> result = new HashMap<>(8);

        Field[] declaredFields = parameterType.getDeclaredFields();

        for (Field field : declaredFields) {
            field.setAccessible(true);

            if (isJavaClass(field.getType())) {
                result.put(field.getName(), field.getType().getName());
            } else {
                Map<String, String> fieldMap = customerBean(field.getType());
                result.put(field.getName(), new Gson().toJson(fieldMap));
            }
        }
        return result;
    }

    public boolean isGenerics(Type target) {
        return target instanceof ParameterizedType;
    }

    public boolean isServletSubClass(Class<?> clazz) {
        return ServletRequest.class.isAssignableFrom(clazz) || ServletResponse.class.isAssignableFrom(clazz);
    }

    private boolean isJavaClass(Class<?> parameterType) {
        return parameterType != null && parameterType.getClassLoader() == null;
    }
}
