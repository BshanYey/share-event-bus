package cn.share.event.bus.client.register;

import cn.share.event.bus.client.annotation.EventRegister;
import cn.share.event.bus.client.entity.EventNode;
import cn.share.event.bus.client.entity.Param;
import cn.share.event.bus.client.entity.RestNode;
import cn.share.event.bus.client.enums.RegisterType;
import cn.share.event.bus.client.store.DataStorage;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
                // 注册
                DataStorage.registerNode(null);
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

        if (RegisterType.MQ == registerType) {
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
        }
    }

    private Map<String, Object> handlerInput(RestNode node, Method method) {
        Map<String, Object> result = new HashMap<>(4);

        if (method.isAnnotationPresent(RequestMapping.class)) {
            doParseRequestMapping(node, method);
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            doParsePostMapping(node, method);
        } else if (method.isAnnotationPresent(GetMapping.class)) {
            doParseGetMapping(node, method);
        } else {
            // todo 暂时不支持该请求
        }

        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);

            String path = annotation.value()[0];
            // 表示存在路径值
            if (StringUtils.contains(path, LEFT_LABEL) && StringUtils.contains(path, RIGHT_LABEL)) {
                node.setIncludePathValue(true);
            } else {
                node.setIncludePathValue(false);
            }
            RequestMethod httpMethod = annotation.method()[0];

            Class<?>[] parameterTypes = method.getParameterTypes();

            if (RequestMethod.POST == httpMethod) {
                node.setHttpMethod(String.valueOf(RequestMethod.POST));
            } else if (RequestMethod.GET == httpMethod){
                node.setHttpMethod(String.valueOf(RequestMethod.GET));
            } else {
                // todo 暂不支持其他类型请求
            }

            Type[] genericParameterTypes = method.getGenericParameterTypes();

            for (Type type : genericParameterTypes) {
                Param param = new Param();
                // 带泛型类
                if (type instanceof ParameterizedType) {

                } else {
                    Class<?> clazz = (Class<?>) type;
                    if (isJavaClass(clazz)) {

                    } else {
                        Map<String, String> paramMap = customerBean(clazz);
                        param.setParam(new Gson().toJson(paramMap));
                        param.setJavaClass(false);
                    }
                }
            }


            int index = 1;
            for (Class<?> parameterType : parameterTypes) {
                Param param = new Param();
                param.setParamType(parameterType.getName());
                if (isJavaClass(parameterType)) {
                    if (parameterType == List.class) {
                        ParameterizedType type = (ParameterizedType) parameterType.getGenericSuperclass();
                        Type actualTypeArgument = type.getActualTypeArguments()[0];


                    } else if (parameterType == Set.class) {

                    } else {
                        param.setParam(method.getName());
                        param.setJavaClass(true);
                    }
                } else {
                    Map<String, String> paramMap = customerBean(parameterType);
                    param.setParam(new Gson().toJson(paramMap));
                    param.setJavaClass(false);
                }
                param.setParamOrder(index ++);
            }

        } else if (method.isAnnotationPresent(PostMapping.class)) {

        } else {

        }
        return null;
    }

    private void doParseGetMapping(RestNode node, Method method) {

    }

    private void doParsePostMapping(RestNode node, Method method) {

    }

    private void doParseRequestMapping(RestNode node, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        // 构建uri
        doBuildUri(node, requestMapping);

        // 判断是否存在 pathValue
        judgePathValue(node);

        // 获取请求类型
        httpRequestType(requestMapping, node);

        // 封装参数
        buildReqParam(node, method);
    }

    private void buildReqParam(RestNode node, Method method) {

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

    private void doBuildUri(RestNode node, RequestMapping requestMapping) {
        String path = requestMapping.value()[0];

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

    private boolean isJavaClass(Class<?> parameterType) {
        return parameterType != null && parameterType.getClassLoader() == null;
    }
}
