package cn.share.event.bus.server.infrastructure.config;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author gonz
 * @version 1.0
 * @description: ProducerSetConfig
 * @date 2023/6/20 13:48
 */
@Configuration
public class ProducerSetConfig {

    /**
     * MQ生产者实例初始化，从持久化的地方获取信息来初始化  TODO 此处每条生产线对应一个生产者，后续池化
     * key：生产线id
     * value：生产线对应producer实例
     *
     * @return
     */
    @Bean("producerMaps")
    public HashMap<Long, DefaultMQProducer> initProducers(){
        // todo 获取持久化信息, 根据持久化数据设置初始值
        HashMap<Long, DefaultMQProducer> res = new HashMap<>();

        return res;
    }
    @Bean("consumerMaps")
    public HashMap<Long, DefaultMQPushConsumer> initConsumer(){
        // todo 获取持久化信息, 根据持久化数据设置初始值
        HashMap<Long, DefaultMQPushConsumer> res = new HashMap<>();

        return res;
    }
}
