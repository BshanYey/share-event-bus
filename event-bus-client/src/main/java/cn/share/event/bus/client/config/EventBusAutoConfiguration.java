package cn.share.event.bus.client.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 14:39:40
 * @describe class responsibility
 */
@Configuration
@ComponentScan(basePackages = "cn.share.event.bus.client")
@EnableConfigurationProperties(EvenBusProperties.class)
public class EventBusAutoConfiguration {

}
