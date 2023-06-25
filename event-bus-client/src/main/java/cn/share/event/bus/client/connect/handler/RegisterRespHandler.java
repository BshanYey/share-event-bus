package cn.share.event.bus.client.connect.handler;

import cn.share.event.bus.client.proto.TransferProto;
import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-25 18:00:52
 * @describe class responsibility
 */
@Slf4j
@ChannelHandler.Sharable
public class RegisterRespHandler extends BaseHandler<TransferProto.Transfer> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TransferProto.Transfer msg) throws Exception {
        if (isCurrentNode(TransferProto.BusinessType.REGISTER_RESPONSE, msg)) {
            log.info("注册服务信息响应参数:{}", JSON.toJSONString(msg));
            // todo 响应
        } else {
            super.channelRead(ctx, msg);
        }
    }
}
