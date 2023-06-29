package cn.share.event.bus.client.register.connect.handler;

import cn.share.event.bus.client.register.connect.manager.ClientTemplate;
import cn.share.event.bus.client.proto.TransferProto;
import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-25 17:29:25
 * @describe class responsibility
 */
@Slf4j
@ChannelHandler.Sharable
public class SyncPullRespHandler extends BaseHandler<TransferProto.Transfer> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TransferProto.Transfer msg) throws Exception {
        if (isCurrentNode(TransferProto.BusinessType.SYNC_PULL_RESPONSE, msg)) {
            log.info("数据推送响应结果: {}", JSON.toJSONString(msg));
            ClientTemplate.find(msg.getReqId()).complete(msg);
        } else {
           super.channelRead(ctx, msg);
        }
    }
}
