package cn.share.event.bus.server.adapter.nio.handler;

import cn.hutool.extra.spring.SpringUtil;
import cn.share.event.bus.proto.TransferProto;
import cn.share.event.bus.server.application.dto.ProductNodeDto;
import cn.share.event.bus.server.application.service.ProductionNodeAppService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 13:47:44
 * @describe class responsibility
 */
@Slf4j
@ChannelHandler.Sharable
public class RegisterNodeHandler extends BaseHandler<TransferProto.Transfer> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TransferProto.Transfer msg) throws Exception {
        if (isCurrentNode(TransferProto.BusinessType.REGISTER_REQUEST, msg)) {
            log.info("Client :{} register node.", ctx.channel().remoteAddress());
            // todo 日志记录
            ProductionNodeAppService appService = SpringUtil.getBean(ProductionNodeAppService.class);
            // todo 注册信息
            appService.register(new ProductNodeDto());
        } else {
            super.channelRead(ctx, msg);
        }
    }
}
