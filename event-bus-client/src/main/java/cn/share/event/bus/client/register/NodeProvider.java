package cn.share.event.bus.client.register;

import cn.share.event.bus.client.enums.RegisterType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-27 14:00:41
 * @describe class responsibility
 */
public class NodeProvider {

    final static Map<RegisterType, NodeParserStrategy> PARSER_MAP = new HashMap<>();

    static {
        PARSER_MAP.put(RegisterType.MQ, new EventNodeParserStrategy());
        PARSER_MAP.put(RegisterType.REST, new RestNodeParserStrategy());
    }

    public static NodeParserStrategy getNodeParse(RegisterType registerType) {
        if (registerType == null || PARSER_MAP.containsKey(registerType)) {
            throw new RuntimeException("找不到Node注册事件类型.[" + registerType + "]");
        }
        return PARSER_MAP.get(registerType);
    }
}
