package cn.share.event.bus.client.register.nodeparse;

import cn.share.event.bus.client.annotation.EventRegister;
import cn.share.event.bus.client.entity.param.Node;
import cn.share.event.bus.client.entity.parser.ParseNode;
import com.google.gson.Gson;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-27 13:53:47
 * @describe class responsibility
 */
public abstract class NodeParserStrategy<T extends ParseNode> {
    /**
     *  解析对象
     *
     * @param obj 解析对象
     * @return 解析结果
     */
    public abstract Node parse(T obj);


    protected Map<String, String> convertField(Class<?> clazz) {
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

    protected void fullNodeByAnnotation(Node node, EventRegister eventRegister) {
        node.setNodeName(eventRegister.funcName());
        node.setDesc(eventRegister.desc());
        node.setVersion(eventRegister.version());
        node.setRegisterType(String.valueOf(eventRegister.registerType()));
    }

    protected boolean isGenerics(Type target) {
        return target instanceof ParameterizedType;
    }

    protected boolean isServletSubClass(Class<?> clazz) {
        return ServletRequest.class.isAssignableFrom(clazz) || ServletResponse.class.isAssignableFrom(clazz);
    }

    protected boolean isJavaClass(Class<?> parameterType) {
        return parameterType != null && parameterType.getClassLoader() == null;
    }
}
