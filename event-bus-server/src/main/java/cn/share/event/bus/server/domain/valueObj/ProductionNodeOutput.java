package cn.share.event.bus.server.domain.valueObj;

import lombok.Data;

/**
 * @author gonz
 * @version 1.0
 * @description: 生产节点输出
 * @date 2023/6/20 9:41
 */
@Data
public class ProductionNodeOutput {
    /**
     * 业务线id
     */
    private Long productionLineId;

    /**
     * 业务节点id
     */
    private Long productionNodeId;

    /**
     * 业务线实例id
     */
    private Long pLInstanceId;

    /**
     * 功能数据规范
     */
    private String dataSpecification;

    /**
     * 功能数据
     */
    private String data;

    /**
     * 判断当前输出是否是可以有效的流转
     * @return true or false
     */
    public boolean isEffective(){
        // 3个id均有值则认为是有效的可以流转的信息
        return productionLineId != null && productionNodeId != null && pLInstanceId != null;
    }
}
