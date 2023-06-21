package cn.share.event.bus.server.adapter.nio.handler;

import cn.share.event.bus.proto.TransferProto;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 14:06:32
 * @describe class responsibility
 */
@Slf4j
@ChannelHandler.Sharable
public class DefaultAfterPostHandler extends BaseHandler<TransferProto.Transfer> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TransferProto.Transfer msg) throws Exception {
        log.info("Client : {} send invalid request, request msg: {}", ctx.channel().remoteAddress(), JSON.toJSON(msg));
    }
}
