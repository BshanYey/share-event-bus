package cn.share.event.bus.server.application.service;

import cn.share.event.bus.server.application.dto.ProductionLineDto;

/**
 * @author gonz
 * @version 1.0
 * @description: 生产线应用服务
 * @date 2023/6/20 9:07
 */
public interface ProductionLineAppService {

//    /**
//     * 查询生产线
//     */
//    void showAllProductionLine();
    /**
     * 创建生产线
     */
    void createProductionLine(ProductionLineDto productionLineDto);

    /**
     * 升级生产线
     */
    void upLevelProductionLine(ProductionLineDto productionLineDto);

    /**
     * 发布生产线
     */
    void publishProductionLine();

    /**
     * 创建一个生产过程
     */
    void createProductionProcess();
}
