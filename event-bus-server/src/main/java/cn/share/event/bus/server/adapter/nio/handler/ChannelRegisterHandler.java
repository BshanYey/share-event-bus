package cn.share.event.bus.server.adapter.nio.handler;

import cn.share.event.bus.proto.TransferProto;
import cn.share.event.bus.server.adapter.nio.channel.ChannelGroups;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 10:14:09
 * @describe class responsibility
 */
@Slf4j
@ChannelHandler.Sharable
public class ChannelRegisterHandler extends BaseHandler<TransferProto.Transfer> {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("Client: {} register success.", ctx.channel().remoteAddress());
        ChannelGroups.register(ctx.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("Client {} unregister.", ctx.channel().remoteAddress());
        ChannelGroups.discard(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TransferProto.Transfer transfer) throws Exception {
        super.channelRead(channelHandlerContext, transfer);
    }
}
