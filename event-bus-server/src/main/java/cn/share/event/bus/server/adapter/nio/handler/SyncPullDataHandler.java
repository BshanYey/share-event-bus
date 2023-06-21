package cn.share.event.bus.server.adapter.nio.handler;

import cn.share.event.bus.proto.TransferProto;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 13:54:53
 * @describe class responsibility
 */
@Slf4j
@ChannelHandler.Sharable
public class SyncPullDataHandler extends BaseHandler<TransferProto.Transfer> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TransferProto.Transfer msg) throws Exception {
        if (isCurrentNode(TransferProto.BusinessType.SYNC_PULL_REQUEST, msg)) {

        } else {
            super.channelRead(ctx, msg);
        }
    }
}
