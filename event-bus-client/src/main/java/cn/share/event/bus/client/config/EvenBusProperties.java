package cn.share.event.bus.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 14:18:12
 * @describe class responsibility
 */
@Data
@ConfigurationProperties("event.bus.server")
public class EvenBusProperties {
    /**
     *  默认端口号
     */
    private Integer port = 9906;

    /**
     *  总线IP
     */
    private String address;
}
