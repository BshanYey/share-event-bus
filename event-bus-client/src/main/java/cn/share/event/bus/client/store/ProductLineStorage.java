package cn.share.event.bus.client.store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProductLineStorage {

    private static final Map<String, String> PRODUCT_LINE_STORAGE = new ConcurrentHashMap<>(64);

    private static final Map<String, String> NODE_REGISTER_CACHE = new ConcurrentHashMap<>(64);

    private static final Map<String, String> HEAD_NODE_CACHE = new ConcurrentHashMap<>(64);


    public static void registerNode() {

    }
}
