package cn.share.event.bus.server.infrastructure.api.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @author gonz
 * @version 1.0
 * @description: MQ生产者工厂
 * @date 2023/6/20 11:43
 */
@Slf4j
@Component
public class RocketMQConsumerFactory {

    @Value("${mq.address}")
    private String address;

    @Resource(name = "consumerMaps")
    HashMap<Long, DefaultMQPushConsumer> consumerMap;

    /**
     * 为一个生产线创建一个producer
     * @param productionLineId 业务线id
     * @return producer
     * @throws MQClientException 创建producer异常
     */
    public DefaultMQPushConsumer buildConsumer(Long productionLineId) throws MQClientException {
        try {
            //设置消费者组名
            String consumerGroup = String.valueOf(productionLineId) + new Random().nextInt(Integer.MAX_VALUE);
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
            //设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            //NameServer地址， 多个地址用分号(;)分隔
            consumer.setNamesrvAddr(address);
            //参数1：topic名字 参数2：tag名字
            String topic = "production-line-" + productionLineId;
            String tag = topic;
            consumer.subscribe(topic, tag);
            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
                for (MessageExt msg : msgs) {
                    String messageBody = new String(msg.getBody());
                    log.info("生产线：{}，收到消息：{}", productionLineId, messageBody);
                    // todo 生产线的生产节点流转 实际流转逻辑应写在app，这里做调用
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            //启动，会一直监听消息
            consumer.start();
            consumerMap.put(productionLineId, consumer);
            System.out.println("Consumer Started!");
            return consumer;
        } catch (MQClientException e){
            log.error("MQ消费者者创建失败，失败原因：{}", e.getMessage());
            throw e;
        }
    }
}
