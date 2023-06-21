package cn.share.event.bus.client.connect.handler;

import cn.share.event.bus.client.proto.TransferProto;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 17:00:11
 * @describe class responsibility
 */
@Slf4j
@ChannelHandler.Sharable
public class HeartBeatHandler extends BaseHandler<TransferProto.Transfer> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Event bus client connect server success, ready send heart beat request.");
        // 构建请求
        TransferProto.Transfer transfer = buildTransfer();

        ctx.executor()
                .scheduleAtFixedRate(
                        () -> ctx.channel().writeAndFlush(transfer),
                        4,
                        4,
                        TimeUnit.SECONDS);
        // 心跳请求
        ctx.channel().writeAndFlush(transfer);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TransferProto.Transfer msg) throws Exception {
        if (isCurrentNode(TransferProto.BusinessType.HEART_BEAT_RESPONSE,  msg)) {

        }
    }

    private TransferProto.Transfer buildTransfer() {
        return TransferProto.Transfer.newBuilder()
                .setReqId(UUID.randomUUID().toString().replace("-", ""))
                .setReqTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .setBusinessType(TransferProto.BusinessType.HEART_BEAT_REQUEST)
                .build();
    }
}
