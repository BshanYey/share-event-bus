package cn.share.event.bus.client.connect;

import cn.share.event.bus.client.config.EvenBusProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 14:43:13
 * @describe class responsibility
 */
@Component
public class ClientManagerFactory implements CommandLineRunner {

    @Resource
    EvenBusProperties properties;

    @Override
    public void run(String... args) throws Exception {
        IClient iClient = IClient.Factory.create();
        iClient.connect(properties.getPort(), properties.getAddress());
    }
}
