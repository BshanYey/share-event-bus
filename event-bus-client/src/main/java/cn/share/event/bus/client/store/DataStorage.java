package cn.share.event.bus.client.store;

import cn.share.event.bus.client.entity.param.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-26 14:37:00
 * @describe class responsibility
 */
public class DataStorage {

    private static final Map<String, String> PRODUCT_LINE_STORAGE = new ConcurrentHashMap<>(64);

    private static final Map<String, List<Node>> NODE_REGISTER_CACHE = new ConcurrentHashMap<>(64);

    private static final Map<String, String> HEAD_NODE_CACHE = new ConcurrentHashMap<>(64);


    public static void registerNode(Node node) {
        NODE_REGISTER_CACHE.computeIfAbsent(node.getNodeName(), k -> new ArrayList<>()).add(node);
    }

    public static Map<String, List<Node>> allRegisterNodes() {
        return NODE_REGISTER_CACHE;
    }
}
