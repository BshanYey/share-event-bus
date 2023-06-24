package cn.share.event.bus.server.application.service.impl;

import cn.share.event.bus.server.application.dto.ProductionLineDto;
import cn.share.event.bus.server.application.service.ProductionLineAppService;
import cn.share.event.bus.server.domain.aggregate.ProductionLine;
import cn.share.event.bus.server.domain.aggregate.ProductionNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author gonz
 * @version 1.0
 * @description: ProductionLineAppServiceImpl
 * @date 2023/6/23 17:43
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductionLineAppServiceImpl implements ProductionLineAppService {

    @Override
    public void createProductionLine(ProductionLineDto productionLineDto) {
        // todo 这里需要check相同的头结点是否被其他生产线占用，如果被占用则不能创建新的流水线，只能升级流水线
        // TODO 这里缺失将dto里边的节点树转换成entity里边需要的结构
        ProductionNode nodeHead = null;
        HashMap<Long, ProductionNode> lineContent = new HashMap<>();
        new ProductionLine(
                productionLineDto.getName(),
                productionLineDto.getVersion(),
                productionLineDto.getDescription(),
                nodeHead,
                lineContent
        ).createAndPublish();
    }

    @Override
    public void upLevelProductionLine(ProductionLineDto productionLineDto) {

    }

    @Override
    public void publishProductionLine() {

    }

    @Override
    public void createProductionProcess() {
        // todo
    }

}
