package cn.share.event.bus.client.register.connect.impl;

import cn.share.event.bus.client.register.connect.IClient;
import cn.share.event.bus.client.register.connect.handler.ClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 15:03:09
 * @describe class responsibility
 */
public abstract class AbstractClient<T extends Channel> implements IClient, ApplicationContextAware, DisposableBean {

    protected static final Integer WORKER_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    protected EventLoopGroup worker;

    protected Bootstrap bootstrap;

    protected ApplicationContext applicationContext;

    public AbstractClient() {
        this.bootstrap = new Bootstrap();
    }

    protected void configBootStrap() {
        this.bootstrap.group(worker)
                .channel(socketChannel())
                .option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ClientInitializer());
    }

    /**
     *  socket连接渠道
     *
     * @return 返回对应渠道class
     */
    protected abstract Class<T> socketChannel();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        this.worker.shutdownGracefully();
    }
}
