package cn.share.event.bus.server.domain.valueObj;

import lombok.AllArgsConstructor;

import java.util.Arrays;

/**
 * @author gonz
 * @version 1.0
 * @description: 生产节点状态枚举
 * @date 2023/6/20 9:17
 */

@AllArgsConstructor
public enum ProductionNodeStatusEnum {
    /**
     * 在线
     */
    ON_LINE("在线", 1),
    OFF_LINE("离线", 0)
    ;

    private final String name;
    private final Integer value;
    public String getName() {
        return this.name;
    }

    public Integer getValue() {
        return this.value;
    }

    public static ProductionNodeStatusEnum fromValue(Integer value){
        return Arrays.stream(ProductionNodeStatusEnum.values())
                .filter(e->e.getValue().equals(value)).findFirst().orElse(null);
    }
}

