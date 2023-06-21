package cn.share.event.bus.server.adapter.nio.handler;

import cn.share.event.bus.proto.TransferProto;
import cn.share.event.bus.server.adapter.nio.channel.ChannelGroups;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.time.LocalDateTime;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 09:34:31
 * @describe class responsibility
 */
@Slf4j
@ChannelHandler.Sharable
public class HeartBeatHandler extends BaseHandler<TransferProto.Transfer> {

    private static final Integer HEART_BEAT_FAILED_THRESHOLD = 3;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TransferProto.Transfer transfer) throws Exception {
        if (isCurrentNode(TransferProto.BusinessType.HEART_BEAT_REQUEST, transfer)) {
            SocketAddress socketAddress = ctx.channel().remoteAddress();
            log.info("Receive client socket info :{} heart beat request.", socketAddress);

            TransferProto.Response response = TransferProto.Response.newBuilder()
                    .setCode(200)
                    .setMessage("Heart beat handler success.")
                    .build();

            TransferProto.Transfer.Builder transferResp = TransferProto.Transfer.newBuilder()
                    .setReqId(transfer.getReqId())
                    .setBusinessType(TransferProto.BusinessType.HEART_BEAT_RESPONSE)
                    .setReqTime(LocalDateTime.now().format(FORMATTER))
                    .setResponse(response);

            ctx.channel().writeAndFlush(transferResp);
        } else {
            // 不是当前节点,则直接跳过
            super.channelRead(ctx, transfer);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;

            if (event == IdleStateEvent.ALL_IDLE_STATE_EVENT) {
                ChannelGroups.addCounter(ctx.channel());
                log.error("Client: {} in 5 seconds not heart beat.", ctx.channel().remoteAddress());

                if (ChannelGroups.getCounter(ctx.channel()) > HEART_BEAT_FAILED_THRESHOLD) {
                    log.error("Client heart beat failed count gt 3, auto close connect.");
                    ChannelGroups.discard(ctx.channel());
                }
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ChannelGroups.discard(ctx.channel());
    }
}
