package cn.share.event.bus.server.adapter.nio.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dengencoding@gail.com
 * @version v1.0
 * @date 2023-06-20 23:39:48
 * @describe class responsibility
 */
@Slf4j
public class EpollServer extends AbstractServer<EpollServerSocketChannel> {
    public EpollServer() {
        super();
        super.boss = new EpollEventLoopGroup(1);
        super.worker = new EpollEventLoopGroup(WORKER_POOL_SIZE);
    }

    @Override
    public void start(Integer port) {
        this.bootStrapConfig();
        try {
            String localHost = super.doGetLocalHost();

            ChannelFuture future = super.serverBootstrap.bind(localHost, port).sync();
            log.info("Listener start to ip:{} port: {} success.", localHost, port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("服务启动异常", e);
            throw new RuntimeException(e.getMessage());
        } finally {
            super.boss.shutdownGracefully();
            super.worker.shutdownGracefully();
        }
    }

    @Override
    public Class<EpollServerSocketChannel> socketChannel() {
        return EpollServerSocketChannel.class;
    }
}
