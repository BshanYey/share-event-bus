package cn.share.event.bus.server.application.service.impl;

import cn.share.event.bus.server.application.converter.ProductionNodeConverter;
import cn.share.event.bus.server.application.dto.ProductionNodeFlowDto;
import cn.share.event.bus.server.application.service.ProductionProcessAppService;
import cn.share.event.bus.server.domain.ability.ProductionDomainService;
import cn.share.event.bus.server.domain.entity.ProductionProcessNode;
import cn.share.event.bus.server.domain.valueObj.ProductionNodeOutput;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author gonz
 * @version 1.0
 * @description: ProductionProcessAppServiceImpl
 * @date 2023/6/24 11:14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductionProcessAppServiceImpl implements ProductionProcessAppService {

    private final ProductionNodeConverter productionNodeConverter;
    private final ProductionDomainService plDomainService;

    /**
     * 流水线节点流转
     *  1、记录当前调用
     *  2、查询下个（多个）节点
     *  3、流转
     * @param nodeFlow 前一个节点的输出
     */
    @Override
    public void processFlow(ProductionNodeFlowDto nodeFlow) {
        log.info("节点流转开始，输入：{}", new Gson().toJson(nodeFlow));
        ProductionNodeOutput thisNodeOut = productionNodeConverter.flowDto2Pnil(nodeFlow);
        if(!thisNodeOut.isEffective()){
            // 非健康的可流转的节点输出
            log.info("当前节点输出为非健康的可流转的节点输出，信息：{}", new Gson().toJson(thisNodeOut));
            return;
        }
        // 获取流转信息然后流转
        ProductionProcessNode productionNodeInLine = plDomainService.getProductionNodeInLine(thisNodeOut);
        productionNodeInLine.flow();
    }

}
