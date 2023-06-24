package cn.share.event.bus.server.application.service;

import cn.share.event.bus.server.application.dto.ProductionNodeFlowDto;

/**
 * @author gonz
 * @version 1.0
 * @description: ProductionProccessAppService
 * @date 2023/6/24 11:13
 */
public interface ProductionProcessAppService {

    /**
     * 业务线流转
     * @param nodeFlow 前一个节点的输出
     */
    void processFlow(ProductionNodeFlowDto nodeFlow);

}
