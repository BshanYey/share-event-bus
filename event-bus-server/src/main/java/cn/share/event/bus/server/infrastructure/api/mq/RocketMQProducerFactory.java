package cn.share.event.bus.server.infrastructure.api.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author gonz
 * @version 1.0
 * @description: MQ生产者工厂
 * @date 2023/6/20 11:43
 */
@Slf4j
@Component
public class RocketMQProducerFactory {

    @Value("${mq.address}")
    private String address;

    @Resource(name = "producerMaps")
    HashMap<Long, DefaultMQProducer> producerMaps;

    /**
     * 为一个生产线创建一个producer
     * @param productionLineId 业务线id
     * @return producer
     * @throws MQClientException 创建producer异常
     */
    public DefaultMQProducer buildProducer(Long productionLineId) throws MQClientException {
        try {
            //设置生产者组名
            String producerGroup = "production-line-" + productionLineId;
            DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
            //指定nameServer的地址, 多个地址用分号分隔
            producer.setNamesrvAddr(address);
            //启动实例
            producer.start();
            producerMaps.put(productionLineId, producer);
            return producer;
        } catch (Exception e) {
            log.error("MQ生产者创建失败，失败原因：{}", e.getMessage());
            throw e;
        }
    }
}
