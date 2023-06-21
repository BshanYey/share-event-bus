package cn.share.event.bus.server.adapter.nio.server;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author dengencoding@gail.com
 * @version v1.0
 * @date 2023-06-20 23:06:13
 * @describe class responsibility
 */

public interface IServer {
    /**
     *  服务启动
     *
     * @param port 监听端口
     */
    void start(Integer port);

    @Slf4j
    class Factory {

        static final String DEFAULT_IO_MODEL = "nio";
        static final String DEFAULT_EPOLL_MODEL = "epoll";

        public static IServer create() {
            String model = System.getProperty("event.bus.io.model");
            String ioModel = StringUtils.isBlank(model) ? DEFAULT_IO_MODEL : model;

            if (!StringUtils.containsAny(ioModel, DEFAULT_IO_MODEL, DEFAULT_EPOLL_MODEL)) {
                ioModel = DEFAULT_IO_MODEL;
            }
            log.info("Event bus use io model : {} ", ioModel);

            if (StringUtils.equalsIgnoreCase(DEFAULT_IO_MODEL, ioModel)) {
                return new NioServer();
            } else if (StringUtils.equalsIgnoreCase(DEFAULT_EPOLL_MODEL, ioModel)) {
                return new EpollServer();
            }
            return null;
        }
    }
}
