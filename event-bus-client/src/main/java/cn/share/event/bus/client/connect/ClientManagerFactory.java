package cn.share.event.bus.client.connect;

import cn.share.event.bus.client.config.EvenBusProperties;
import javax.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 14:43:13
 * @describe class responsibility
 */
@Component
public class ClientManagerFactory implements InitializingBean {

    @Resource
    EvenBusProperties properties;

    @Override
    public void afterPropertiesSet() throws Exception {
        CompletableFuture.runAsync(() -> {
            IClient iClient = IClient.Factory.create();
            iClient.connect(properties.getPort(), properties.getAddress());
        });
    }
}
