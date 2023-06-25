package cn.share.event.bus.server.application.service.impl;

import cn.share.event.bus.server.application.dto.ProductNodeDto;
import cn.share.event.bus.server.application.service.ProductionNodeAppService;
import cn.share.event.bus.server.domain.aggregate.ProductionNode;

import java.util.List;

/**
 * @author gonz
 * @version 1.0
 * @description: ProductionNodeAppServiceImpl
 * @date 2023/6/23 17:31
 */
public class ProductionNodeAppServiceImpl implements ProductionNodeAppService {

    @Override
    public void register(ProductNodeDto productNodeDto) {
        try{
            // todo 创建一个的生产节点
//            ProductionNode pn = new ProductionNode();
//            pn.register();
        } catch (Exception e){

        }
    }

    @Override
    public List<ProductionNode> showProductionNode() {
        return null;
    }

    @Override
    public void offlineProductionNode() {

    }
}
