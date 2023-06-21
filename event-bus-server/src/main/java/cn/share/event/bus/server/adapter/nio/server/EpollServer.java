package cn.share.event.bus.server.adapter.nio.server;

import io.netty.channel.epoll.EpollEventLoopGroup;

/**
 * @author dengencoding@gail.com
 * @version v1.0
 * @date 2023-06-20 23:39:48
 * @describe class responsibility
 */
public class EpollServer extends AbstractServer {
    public EpollServer() {
        super();
        super.boss = new EpollEventLoopGroup(1);
        super.worker = new EpollEventLoopGroup(WORKER_POOL_SIZE);
    }

    @Override
    public void start(Integer port) {
        this.bootStrapConfig();

    }
}
