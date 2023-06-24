package cn.share.event.bus.server.infrastructure.gateway;

import cn.share.event.bus.server.domain.aggregate.ProductionNode;
import cn.share.event.bus.server.domain.gateway.ProductionNodeGateway;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Random;

/**
 * @author gonz
 * @version 1.0
 * @description: PNByMemoryGatewayImpl
 * @date 2023/6/24 10:47
 */
@Component
public class PNByMemoryGatewayImpl implements ProductionNodeGateway {

    private HashMap<String, ProductionNode> productionNodeSet = new HashMap<>();

    @Override
    public Long save(ProductionNode productionNode) {
        String key = productionNode.getName() + productionNode.getServiceName() + productionNode.getVersion();
        ProductionNode productionNodeBySet = productionNodeSet.get(key);
        if(productionNodeBySet == null){
            return productionNode.getId();
        }
        Long id = new Random().nextLong();
        productionNodeSet.put(key, productionNode);
        return id;
    }

    @Override
    public void updateById(ProductionNode productionNode) {

    }
}
