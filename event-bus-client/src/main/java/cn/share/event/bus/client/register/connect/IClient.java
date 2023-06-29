package cn.share.event.bus.client.register.connect;

import cn.share.event.bus.client.register.connect.impl.EpollClientServer;
import cn.share.event.bus.client.register.connect.impl.NioClientServer;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 14:54:53
 * @describe class responsibility
 */
public interface IClient {

    /**
     *  创建连接
     *
     * @param port 服务端口
     * @param serverHost 服务地址
     */
    void connect(Integer port, String serverHost) throws Exception;

    String DEFAULT_IO_MODEL = "nio";

    String EPOLL_IO_MODEL = "epoll";

    class Factory {

        public static IClient create() {
            String model = System.getProperty("event.bus.io.model");

            String ioModel = StringUtils.isBlank(model) ? DEFAULT_IO_MODEL : model;

            if (!StringUtils.containsAny(ioModel, EPOLL_IO_MODEL, DEFAULT_IO_MODEL)) {
                ioModel = DEFAULT_IO_MODEL;
            }

            if (StringUtils.equals(DEFAULT_IO_MODEL, ioModel)) {
                return new NioClientServer();
            } else if (StringUtils.equals(EPOLL_IO_MODEL, ioModel)) {
                return new EpollClientServer();
            }
            return new NioClientServer();
        }
    }
}
