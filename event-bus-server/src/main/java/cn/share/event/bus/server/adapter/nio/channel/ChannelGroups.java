package cn.share.event.bus.server.adapter.nio.channel;

import cn.share.event.bus.proto.TransferProto;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 10:31:32
 * @describe class responsibility
 */
@Slf4j
public class ChannelGroups {
    private static final Map<String, Channel> CHANNEL_GROUP = new ConcurrentHashMap<>(32);

    private static final Map<String, AtomicInteger> COUNTER_MAP = new ConcurrentHashMap<>(32);

    public static void register(Channel channel) {
        CHANNEL_GROUP.put(channel.id().asLongText(), channel);
    }

    public static void discard(Channel channel) {
        if (contains(channel)) {
            COUNTER_MAP.remove(channel.id().asLongText());
            CHANNEL_GROUP.remove(channel.id().asLongText()).close();
        }
    }

    public static boolean contains(Channel channel) {
        return CHANNEL_GROUP.containsKey(channel.id().asLongText());
    }

    public static void sendMsg(Channel channel, TransferProto.Transfer msg) {
        if (Objects.nonNull(channel) && Objects.nonNull(msg)) {
            channel.writeAndFlush(msg);
        }
    }

    public static void sendMsg(String channelId, TransferProto.Transfer msg) {
        if (CHANNEL_GROUP.containsKey(channelId)) {
            sendMsg(CHANNEL_GROUP.get(channelId), msg);
        } else {
            log.info("ChannelId: {} is not exits.", channelId);
        }
    }

    public static int count() {
        return CHANNEL_GROUP.size();
    }

    public static void addCounter(Channel channel) {
        COUNTER_MAP.computeIfAbsent(channel.id().asLongText(), (key) -> new AtomicInteger(0)).getAndIncrement();
    }

    public static int getCounter(Channel channel) {
        if (COUNTER_MAP.containsKey(channel.id().asLongText())) {
            return COUNTER_MAP.get(channel.id().asLongText()).get();
        } else {
            return 0;
        }
    }
}
