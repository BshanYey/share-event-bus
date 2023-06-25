package cn.share.event.bus.server.infrastructure.config.busserver;

import cn.share.event.bus.server.adapter.nio.server.IServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-25 17:39:24
 * @describe class responsibility
 */
@Component
public class InitBusServer implements SmartLifecycle {

    @Value("${event.bus.port}")
    private Integer port;

    public volatile boolean startFlag = false;

    private IServer iServer;

    @Override
    public void start() {
        CompletableFuture.runAsync(() -> {
            if (!startFlag) {
                iServer = IServer.Factory.create();
                iServer.start(port);
                startFlag = true;
            }
        });
    }

    @Override
    public void stop() {
        iServer.stop();
        startFlag = false;
    }

    @Override
    public boolean isRunning() {
        return startFlag;
    }
}
