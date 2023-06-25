package cn.share.event.bus.client.connect.impl;

import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 15:18:04
 * @describe class responsibility
 */
@Slf4j
public class NioClientServer extends AbstractClient<NioSocketChannel> {

    public NioClientServer() {
        super();
        super.worker = new NioEventLoopGroup(WORKER_POOL_SIZE);
    }

    @Override
    public void connect(Integer port, String serverHost) throws Exception {
        if (StringUtils.isBlank(serverHost)) {
            throw new IllegalAccessException("event.bus.server.address 不允许为空.");
        }
        super.configBootStrap();
        try {
            ChannelFuture channelFuture = super.bootstrap.connect(serverHost, port).sync();
            log.info("Event bus client connect, host:{} port:{} success.", serverHost, port);

            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            super.worker.shutdownGracefully();
        }

    }

    @Override
    protected Class<NioSocketChannel> socketChannel() {
        return NioSocketChannel.class;
    }
}
