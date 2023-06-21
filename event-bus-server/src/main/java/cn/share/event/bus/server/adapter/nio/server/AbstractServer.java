package cn.share.event.bus.server.adapter.nio.server;

import cn.share.event.bus.server.adapter.nio.handler.CustomerChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author dengencoding@gail.com
 * @version v1.0
 * @date 2023-06-20 23:07:29
 * @describe class responsibility
 */
public abstract class AbstractServer implements IServer {

    protected static final Integer WORKER_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    protected EventLoopGroup boss;

    protected EventLoopGroup worker;


    protected ServerBootstrap serverBootstrap;

    public AbstractServer() {
        this.serverBootstrap = new ServerBootstrap();
    }

    public void bootStrapConfig() {
        this.serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                // 连接超时时间
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 15000)
                // 全连接最大连接个数， 超过1024个，则直接拒绝客户端连接
                .option(ChannelOption.SO_BACKLOG, 1024)
                // 开启无延迟配置， 默认使用nagle算法，回导致数据传输延迟
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                // 是否启用心跳保活机制，即连接保活
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 自定义handler处理器
                .childHandler(new CustomerChannelHandler());
    }
}
