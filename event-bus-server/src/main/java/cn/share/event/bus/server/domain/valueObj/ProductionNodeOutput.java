package cn.share.event.bus.server.domain.valueObj;

/**
 * @author gonz
 * @version 1.0
 * @description: 生产节点输出
 * @date 2023/6/20 9:41
 */
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

}
