package cn.share.event.bus.server.adapter.nio.handler;

import cn.share.event.bus.proto.TransferProto;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 14:04:39
 * @describe class responsibility
 */
@Slf4j
@ChannelHandler.Sharable
public class SyncPushResponseHandler extends BaseHandler<TransferProto.Transfer> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TransferProto.Transfer msg) throws Exception {
        if (isCurrentNode(TransferProto.BusinessType.SYNC_PUSH_RESPONSE, msg)) {
            // todo 推送数据响应结果
        } else {
            super.channelRead(ctx, msg);
        }
    }
}
