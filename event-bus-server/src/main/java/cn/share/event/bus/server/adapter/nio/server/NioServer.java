package cn.share.event.bus.server.adapter.nio.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dengencoding@gail.com
 * @version v1.0
 * @date 2023-06-20 23:12:50
 * @describe class responsibility
 */
@Slf4j
public class NioServer extends AbstractServer<NioServerSocketChannel> {
    public NioServer() {
        super();
        super.boss = new NioEventLoopGroup(1);
        super.worker = new NioEventLoopGroup(WORKER_POOL_SIZE);
    }

    @Override
    public void start(Integer port) {
        // 配置基础信息
        this.bootStrapConfig();

        try {
            String localHost = doGetLocalHost();

            ChannelFuture future = super.serverBootstrap.bind(localHost, port).sync();
            log.info("Listener start to ip:{} port: {} success.", localHost, port);

            future.channel().closeFuture().sync();
        } catch (Throwable e) {
            log.error("服务启动异常", e);
            throw new RuntimeException(e.getMessage());
        } finally {
            super.boss.shutdownGracefully();
            super.worker.shutdownGracefully();
        }
    }

    @Override
    public Class<NioServerSocketChannel> socketChannel() {
        return NioServerSocketChannel.class;
    }
}
