package cn.share.event.bus.client.register.connect.handler;

import cn.share.event.bus.client.proto.TransferProto;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 15:17:05
 * @describe class responsibility
 */
@ChannelHandler.Sharable
public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new IdleStateHandler(0, 0, 5, TimeUnit.SECONDS));
        // 请求解密
        ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
        ch.pipeline().addLast(new ProtobufDecoder(TransferProto.Transfer.getDefaultInstance()));

        // 请求加密
        ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
        ch.pipeline().addLast(new ProtobufEncoder());

        // 处理心跳请求
        ch.pipeline().addLast(new HeartBeatHandler());
        // 处理注册结果
        ch.pipeline().addLast(new RegisterRespHandler());
        // 处理拉取结果
        ch.pipeline().addLast(new SyncPullRespHandler());
        // 处理推送结果
        ch.pipeline().addLast(new SyncPushReqHandler());
    }
}
