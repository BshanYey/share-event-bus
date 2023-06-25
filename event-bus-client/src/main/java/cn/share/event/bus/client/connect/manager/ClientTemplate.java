package cn.share.event.bus.client.connect.manager;

import cn.share.event.bus.client.proto.TransferProto;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-25 18:10:54
 * @describe class responsibility
 */
public class ClientTemplate {

    private static volatile Channel channel;

    private static final Map<String, CompletableFuture<TransferProto.Transfer>> REQUEST_CACHE = new ConcurrentHashMap<>(128);

    public static void addChannel(Channel channel) {
        ClientTemplate.channel = channel;
    }


    public static CompletableFuture<TransferProto.Transfer> find(String reqID) {
        return REQUEST_CACHE.get(reqID);
    }

    public static CompletableFuture<TransferProto.Transfer> send(TransferProto.Transfer msg) {
        CompletableFuture<TransferProto.Transfer> completableFuture = new CompletableFuture<>();
        channel.writeAndFlush(msg);
        REQUEST_CACHE.put(msg.getReqId(), completableFuture);
        return completableFuture;
    }

}
