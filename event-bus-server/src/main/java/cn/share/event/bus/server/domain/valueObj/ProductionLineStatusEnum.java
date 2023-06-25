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
public enum ProductionLineStatusEnum {
    /**
     * 在线
     */
    CREATED("已创建", 1),
    PUBLISHED("已发布", 2),
    OFF_LINE_ING("下线中", 3),
    OFF_LINE("已下线", 4),
    DELETED("已删除", 5)
    ;

    private final String name;
    private final Integer value;
    public String getName() {
        return this.name;
    }

    public Integer getValue() {
        return this.value;
    }

    public static ProductionLineStatusEnum fromValue(Integer value){
        return Arrays.stream(ProductionLineStatusEnum.values())
                .filter(e->e.getValue().equals(value)).findFirst().orElse(null);
    }
}

