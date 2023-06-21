package cn.share.event.bus.server.adapter.nio.server;

/**
 * @author dengencoding@gail.com
 * @version v1.0
 * @date 2023-06-20 23:06:13
 * @describe class responsibility
 */
public interface IServer {
    /**
     *  服务启动
     */
    void start(Integer port);
}
