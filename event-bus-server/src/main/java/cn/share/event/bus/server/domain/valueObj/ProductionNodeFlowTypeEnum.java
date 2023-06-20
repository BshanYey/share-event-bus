package cn.share.event.bus.server.domain.valueObj;

import lombok.AllArgsConstructor;

import java.util.Arrays;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-20 18:43:32
 * @describe 节点流转类型枚举
 */
@AllArgsConstructor
public enum ProductionNodeFlowTypeEnum {
    /**
     * 在线
     */
    SYNC("同步调用", 1),
    ASYNC("异步调用", 2)
    ;

    private final String name;
    private final Integer value;
    public String getName() {
        return this.name;
    }

    public Integer getValue() {
        return this.value;
    }

    public static ProductionNodeFlowTypeEnum fromValue(Integer value){
        return Arrays.stream(ProductionNodeFlowTypeEnum.values())
                .filter(e->e.getValue().equals(value)).findFirst().orElse(null);
    }
}
