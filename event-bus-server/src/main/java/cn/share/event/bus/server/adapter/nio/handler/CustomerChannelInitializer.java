package cn.share.event.bus.server.adapter.nio.handler;

import cn.share.event.bus.proto.TransferProto;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author dengencoding@gail.com
 * @version v1.0
 * @date 2023-06-20 23:35:56
 * @describe class responsibility
 */
public class CustomerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new IdleStateHandler(0, 0, 5, TimeUnit.SECONDS));
        // 请求解密
        ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
        ch.pipeline().addLast(new ProtobufDecoder(TransferProto.Transfer.getDefaultInstance()));

        // 请求加密
        ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
        ch.pipeline().addLast(new ProtobufEncoder());

        // 记录注册client请求
        ch.pipeline().addLast(new ChannelRegisterHandler());
        // 服务端心跳检测
        ch.pipeline().addLast(new HeartBeatHandler());
        // 注册节点请求
        ch.pipeline().addLast(new RegisterNodeHandler());
        // 同步拉取处理请求
        ch.pipeline().addLast(new SyncPullDataHandler());
        // 同步推送响应结果
        ch.pipeline().addLast(new SyncPushResponseHandler());
        // 默认后置处理器, 处理无效请求
        ch.pipeline().addLast(new DefaultAfterPostHandler());
    }
}
