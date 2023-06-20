package cn.share.event.bus.server.domain.gateway;

import cn.share.event.bus.server.domain.aggregate.ProductionLine;

/**
 * @author gonz
 * @version 1.0
 * @description: 生产线仓储网关
 * @date 2023/6/20 21:10
 */
public interface ProductionLineGateway {

    /**
     * 存储一条生产线
     * @param productionLine 生产线
     */
    void saveProductionLine(ProductionLine productionLine);

    /**
     * 存储一条生产线
     * @param productionLineId 生产线id
     */
    ProductionLine getById(Long productionLineId);
}
