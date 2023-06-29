package cn.share.event.bus.client.register.connect;

import cn.share.event.bus.client.config.EvenBusProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.concurrent.CompletableFuture;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 14:43:13
 * @describe class responsibility
 */
@Slf4j
@Component
public class ClientManagerFactory implements CommandLineRunner, ApplicationContextAware {

    @Resource
    EvenBusProperties properties;

    ConfigurableApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Override
    public void run(String... args) throws Exception {
        CompletableFuture.runAsync(() -> {
            try {
                IClient iClient = IClient.Factory.create();
                iClient.connect(properties.getPort(), properties.getAddress());
            } catch (Exception e) {
                log.error("Event client connect error.", e);
                applicationContext.close();
            }
        });
    }
}
