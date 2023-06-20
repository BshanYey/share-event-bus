package cn.share.event.bus.server.domain.entity;

import cn.share.event.bus.server.domain.aggregate.ProductionNode;
import cn.share.event.bus.server.domain.valueObj.ProductionNodeFlowChannelEnum;
import cn.share.event.bus.server.domain.valueObj.ProductionNodeFlowTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author gonz
 * @version 1.0
 * @description: 生产线上的生产节点
 * @date 2023/6/20 8:37
 */
@Slf4j
@Getter
@Setter
public class ProductionNodeInLine {

    /**
     * 生产节点
     */
    private ProductionNode productionNode;

    /**
     * 生产节点流转类型，同步 or 异步
     */
    private ProductionNodeFlowTypeEnum flowType;

    /**
     * 生产节点流转通道，MQ、REST
     */
    private ProductionNodeFlowChannelEnum flowChannel;

    /**
     * 前一步节点
     */
    List<ProductionNodeInLine> preNodes;

    /**
     * 下一步的节点
     */
    List<ProductionNodeInLine> nextNodes;

    /**
     * 该节点是否通过，节点出口输出一次记为 TRUE，否则为 FALSE
     */
    public Boolean pass;

    /**
     * 节点通过次数
     */
    public Integer passCount;

}
