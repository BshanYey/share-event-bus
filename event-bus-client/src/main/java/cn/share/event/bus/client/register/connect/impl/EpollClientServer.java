package cn.share.event.bus.client.register.connect.impl;

import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 15:39:02
 * @describe class responsibility
 */
public class EpollClientServer extends AbstractClient<EpollSocketChannel> {

    public EpollClientServer() {
        super();
        super.worker = new EpollEventLoopGroup(WORKER_POOL_SIZE);
    }

    @Override
    public void connect(Integer port, String serverHost) {
        // todo 暂时用不到
    }

    @Override
    protected Class<EpollSocketChannel> socketChannel() {
        return EpollSocketChannel.class;
    }
}
