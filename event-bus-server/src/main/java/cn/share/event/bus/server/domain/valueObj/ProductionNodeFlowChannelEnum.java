package cn.share.event.bus.server.domain.valueObj;

import lombok.AllArgsConstructor;

import java.util.Arrays;

/**
 * @author gonz
 * @version 1.0
 * @description: 节点流转渠道枚举
 * @date 2023/6/20 20:41
 */
@AllArgsConstructor
public enum ProductionNodeFlowChannelEnum {
    /**
     * 在线
     */
    MQ("消息队列", 1),
    REST("rest请求", 2)
    ;

    private final String name;
    private final Integer value;
    public String getName() {
        return this.name;
    }

    public Integer getValue() {
        return this.value;
    }

    public static ProductionNodeFlowChannelEnum fromValue(Integer value){
        return Arrays.stream(ProductionNodeFlowChannelEnum.values())
                .filter(e->e.getValue().equals(value)).findFirst().orElse(null);
    }
}
