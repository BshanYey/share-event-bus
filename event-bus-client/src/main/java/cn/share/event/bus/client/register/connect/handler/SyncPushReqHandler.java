package cn.share.event.bus.client.register.connect.handler;

import cn.share.event.bus.client.proto.TransferProto;
import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-25 17:57:50
 * @describe class responsibility
 */
@Slf4j
@ChannelHandler.Sharable
public class SyncPushReqHandler extends BaseHandler<TransferProto.Transfer> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TransferProto.Transfer msg) throws Exception {
        if (isCurrentNode(TransferProto.BusinessType.SYNC_PUSH_REQUEST, msg)) {
            log.info("处理服务段推送请求: {}", JSON.toJSONString(msg));
            // todo 业务逻辑处理
        } else {
            super.channelRead(ctx, msg);
        }
    }
}
