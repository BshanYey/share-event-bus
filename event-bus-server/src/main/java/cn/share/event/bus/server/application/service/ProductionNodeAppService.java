package cn.share.event.bus.server.application.service;

import cn.share.event.bus.server.domain.aggregate.ProductionNode;

import java.util.List;

/**
 * @author gonz
 * @version 1.0
 * @description: 生产节点应用服务
 * @date 2023/6/20 9:07
 */
public interface ProductionNodeAppService {

    /**
     * 注册一个生产节点
     */
    void register();

    /**
     * 获取生产节点
     */
    List<ProductionNode> showProductionNode();
    /**
     * 下线生产节点
     */
    void offlineProductionNode();
}
