package cn.share.event.bus.server.application.service;

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

}
