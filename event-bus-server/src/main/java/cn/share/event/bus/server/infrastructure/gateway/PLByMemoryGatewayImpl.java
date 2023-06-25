package cn.share.event.bus.server.infrastructure.gateway;

import cn.share.event.bus.server.domain.aggregate.ProductionLine;
import cn.share.event.bus.server.domain.gateway.ProductionLineGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author gonz
 * @version 1.0
 * @description: 生产线gatewayImpl - 基于本地内存
 * @date 2023/6/20 21:12
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PLByMemoryGatewayImpl implements ProductionLineGateway {


    private HashMap<Long, ProductionLine> productionLineSet = new HashMap<>();

    /**
     * 获取
     * @param productionLineId 生产线id
     */
    public ProductionLine getById(Long productionLineId){
        return productionLineSet.get(productionLineId);
    }

    @Override
    public void updateById(ProductionLine productionLineMeta) {

    }

    /**
     * 存储
     * @param productionLine 生产线
     */
    @Override
    public void saveProductionLine(ProductionLine productionLine) {
        productionLineSet.put(productionLine.getId(), productionLine);
    }
}
