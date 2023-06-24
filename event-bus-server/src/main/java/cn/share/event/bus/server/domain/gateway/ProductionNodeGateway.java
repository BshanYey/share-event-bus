package cn.share.event.bus.server.domain.gateway;

import cn.share.event.bus.server.domain.aggregate.ProductionNode;

/**
 * @author gonz
 * @version 1.0
 * @description: ProductionNodeGateway
 * @date 2023/6/24 10:36
 */
public interface ProductionNodeGateway {

    /**
     * 保存节点
     * @param productionNode 节点信息
     * @return 节点id
     */
    Long save(ProductionNode productionNode);

    /**
     * 更新节点信息
     * @param productionNode 节点信息
     */
    void updateById(ProductionNode productionNode);
}
