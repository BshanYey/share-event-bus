package cn.share.event.bus.server.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gonz
 * @version 1.0
 * @description: 生产节点流转dto
 * @date 2023/6/23 18:05
 */
@Data
@NoArgsConstructor
public class ProductionNodeFlowDto {
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

}
