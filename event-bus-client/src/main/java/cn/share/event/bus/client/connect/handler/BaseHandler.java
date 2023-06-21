package cn.share.event.bus.client.connect.handler;


import cn.share.event.bus.client.proto.TransferProto;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.format.DateTimeFormatter;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 09:47:13
 * @describe class responsibility
 */
public abstract class BaseHandler<T> extends SimpleChannelInboundHandler<T> {
    protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    protected boolean isCurrentNode(TransferProto.BusinessType businessType, TransferProto.Transfer msg) {
        return businessType.getNumber() == msg.getBusinessType().getNumber();
    }
}
