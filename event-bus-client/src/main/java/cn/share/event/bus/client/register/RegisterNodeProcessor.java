package cn.share.event.bus.client.register;

import cn.share.event.bus.client.annotation.EventRegister;
import cn.share.event.bus.client.entity.param.Node;
import cn.share.event.bus.client.entity.parser.EventParseNode;
import cn.share.event.bus.client.entity.parser.ParseNode;
import cn.share.event.bus.client.entity.parser.RestParseNode;
import cn.share.event.bus.client.enums.RegisterType;
import cn.share.event.bus.client.store.DataStorage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-26 11:16:09
 * @describe class responsibility
 */
@Slf4j
@Component
public class RegisterNodeProcessor implements InitializingBean {

    private static final String PATH_SUFFIX = "/";

    private static final String DOUBLE_PATH_SUFFIX = "//";

    @Value("server.servlet.context-path:/")
    private String contentPath;


    private final AtomicInteger registerCount = new AtomicInteger(0);

    private final BlockingQueue<Object> registerQueue = new ArrayBlockingQueue<>(1024);


    public void register(Object obj) {
        registerCount.getAndIncrement();
        registerQueue.offer(obj);
    }

    public int alreadyRegisterCount() {
        return registerCount.get();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CompletableFuture.runAsync(this::doStartRegister);
    }

    private void doStartRegister() {
        while (true) {
            Object target = null;
            try {
                target = registerQueue.take();
                // 解析
                parseBean(target);
            } catch (Exception e) {
                if (Objects.nonNull(target)) {
                    log.error("解析Bean: {} 注册信息失败", target.getClass().getName(), e);
                } else {
                    log.error("解析Bean注册信息失败", e);
                }
            }
        }
    }

    private void parseBean(Object take) {
        Class<?> clazz = take.getClass();
        Method[] declaredMethods = clazz.getDeclaredMethods();

        String api = "";
        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            api = controllerPath(clazz);
        }

        for (Method method : declaredMethods) {
            if (!method.isAnnotationPresent(EventRegister.class)) {
                continue;
            }

            parseMethod(api, method);
        }
    }

    private String controllerPath(Class<?> clazz) {
        String controllerPath = clazz.getAnnotation(RequestMapping.class).value()[0];

        String uri = PATH_SUFFIX
                .concat(contentPath)
                .concat(PATH_SUFFIX)
                .concat(controllerPath);

        return uri
                .replaceAll(DOUBLE_PATH_SUFFIX, PATH_SUFFIX)
                .replaceAll(DOUBLE_PATH_SUFFIX, PATH_SUFFIX);
    }

    private void parseMethod(String api, Method method) {
        EventRegister eventRegister = method.getAnnotation(EventRegister.class);

        RegisterType registerType = eventRegister.registerType();

        ParseNode parseNode;
        if (registerType == RegisterType.MQ) {
            parseNode = new EventParseNode();
            parseNode.setMethod(method);
            parseNode.setEventRegister(eventRegister);
        }
        else if (registerType == RegisterType.REST) {
            RestParseNode restParseNode = new RestParseNode();
            restParseNode.setEventRegister(eventRegister);
            restParseNode.setMethod(method);
            restParseNode.setApi(api);
            parseNode = restParseNode;
        }
        else {
            throw new RuntimeException("暂不支持[" + registerType + "]注册类型");
        }
        // 解析封装节点信息
        Node node = NodeProvider.getNodeParse(registerType).parse(parseNode);

        // 注册
        DataStorage.registerNode(node);
    }
}
