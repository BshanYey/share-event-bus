package cn.share.event.bus.server.domain.ability;

import cn.share.event.bus.server.domain.aggregate.ProductionNode;
import cn.share.event.bus.server.domain.entity.ProductionProcessNode;
import cn.share.event.bus.server.domain.valueObj.ProductionNodeOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gonz
 * @version 1.0
 * @description: ProductionLineDomainService
 * @date 2023/6/23 20:33
 */
@Slf4j
@Service
public class ProductionDomainService {

    /**
     * 通过业务线实例和业务节点ID获取业务线中的业务节点
     * @param thisNodeOut 节点输出
     * @return 生产线上的生产节点
     */
    public ProductionProcessNode getProductionNodeInLine(ProductionNodeOutput thisNodeOut){
        // TODO
        return null;
    }

    /**
     * 展示所有的在线的生产节点
     * @return 所有的在线的生产节点
     */
    public List<ProductionNode> showAllProductionNode(){
        // todo 展示所有的在线的生产节点
        return null;
    }
}
