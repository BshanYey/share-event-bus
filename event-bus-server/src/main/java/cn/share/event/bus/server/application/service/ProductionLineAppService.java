package cn.share.event.bus.server.application.service;

import cn.share.event.bus.server.domain.valueObj.ProductionNodeOutput;

/**
 * @author gonz
 * @version 1.0
 * @description: 生产线应用服务
 * @date 2023/6/20 9:07
 */
public interface ProductionLineAppService {

    /**
     * 查询生产线
     */
    void showProductionLine();
    /**
     * 创建生产线
     */
    void createProductionLine();
    /**
     * 发布生产线
     */
    void publishProductionLine();

    /**
     * 业务线流转
     * @param output 前一个节点的输出
     */
    void flow(ProductionNodeOutput output);
}
