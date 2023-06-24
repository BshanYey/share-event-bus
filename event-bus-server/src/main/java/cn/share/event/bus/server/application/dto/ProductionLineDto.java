package cn.share.event.bus.server.application.dto;

import cn.share.event.bus.server.domain.aggregate.ProductionNode;
import cn.share.event.bus.server.domain.valueObj.ProductionLineStatusEnum;
import lombok.Data;

import java.util.List;

/**
 * @author gonz
 * @version 1.0
 * @description: ProductionLineDto
 * @date 2023/6/24 20:31
 */
@Data
public class ProductionLineDto {

    /**
     * 唯一标识
     */
    private Long id;

    /**
     * 名字
     */
    private String name;

    /**
     * 生产线版本
     */
    private String version;

    /**
     * 生产线状态
     */
    private ProductionLineStatusEnum status;

    /**
     * 生产线描述
     */
    private String description;

    /**
     * 生产节点列表
     */
    private List<LineNode> productionList;

    @Data
    class LineNode{
        private int preIndex;
        private int nextIndex;
        private Long nowProductionNodeId;
    }
}
