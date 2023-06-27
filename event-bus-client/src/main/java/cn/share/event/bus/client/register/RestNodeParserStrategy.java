package cn.share.event.bus.client.register;

import cn.share.event.bus.client.annotation.EventRegister;
import cn.share.event.bus.client.entity.param.Node;
import cn.share.event.bus.client.entity.param.RestNode;
import cn.share.event.bus.client.entity.parser.RestParseNode;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-27 13:59:06
 * @describe class responsibility
 */
public class RestNodeParserStrategy extends NodeParserStrategy<RestParseNode> {

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
        if (method.isAnnotationPresent(PostMapping.class)) {

        } else if (method.isAnnotationPresent(GetMapping.class)) {

        } else if (method.isAnnotationPresent(RequestMapping.class)) {

        } else {
            restNode.setWhetherNormal(false);
        }
    }

    private void parseOutput(RestNode restNode, Method method) {
        restNode.setOutput(new Gson().toJson(super.convertField(method.getReturnType())));
    }
}
