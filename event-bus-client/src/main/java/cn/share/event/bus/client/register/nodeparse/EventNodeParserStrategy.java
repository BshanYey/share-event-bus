package cn.share.event.bus.client.register.nodeparse;

import cn.share.event.bus.client.annotation.EventRegister;
import cn.share.event.bus.client.entity.param.EventNode;
import cn.share.event.bus.client.entity.param.Node;
import cn.share.event.bus.client.entity.param.None;
import cn.share.event.bus.client.entity.parser.EventParseNode;
import com.google.gson.Gson;

import java.util.Map;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-27 13:58:46
 * @describe class responsibility
 */
public class EventNodeParserStrategy extends NodeParserStrategy<EventParseNode> {
    @Override
    public Node parse(EventParseNode parseNode) {
        EventRegister eventRegister = parseNode.getEventRegister();

        EventNode eventNode = new EventNode();

        fullNodeByAnnotation(eventNode, eventRegister);

        eventNode.setTopic(eventRegister.topic());
        eventNode.setInput(buildInput(eventRegister));

        return eventNode;
    }

    private String buildInput(EventRegister eventRegister) {
        Class<?> requestType = eventRegister.requestType();
        if (requestType == None.class) {
            throw new RuntimeException("MQ 节点注册必须有请求参数节点: @EventRegister(requestType= xxxx.class)");
        }
        Map<String, String> fieldMap = super.convertField(requestType);
        return new Gson().toJson(fieldMap);
    }
}
