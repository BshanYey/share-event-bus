package cn.share.event.bus.server.adapter.nio.server;

import cn.share.event.bus.server.global.utils.IpUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.SocketException;

/**
 * @author dengencoding@gail.com
 * @version v1.0
 * @date 2023-06-20 23:12:50
 * @describe class responsibility
 */
@Slf4j
public class NioServer extends AbstractServer {
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
            log.error("服务启动异常");
            throw new RuntimeException(e);
        } finally {
            super.boss.shutdownGracefully();
            super.worker.shutdownGracefully();
        }
    }

    private static String doGetLocalHost() throws SocketException {
        return IpUtil.getLocalIp4Address()
                .map(Inet4Address::getHostAddress)
                .orElseThrow(() -> new RuntimeException("获取当前主机IP异常"));
    }
}
