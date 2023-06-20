package cn.share.event.bus.server.domain.aggregate;

import cn.hutool.extra.spring.SpringUtil;
import cn.share.event.bus.server.domain.entity.ProductionNodeInLine;
import cn.share.event.bus.server.domain.gateway.ProductionLineGateway;
import cn.share.event.bus.server.domain.valueObj.ProductionLineStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * @author gonz
 * @version 1.0
 * @description: 生产线聚合根
 * @date 2023/6/20 8:37
 */
@Slf4j
@Data
@NoArgsConstructor
public class ProductionLine {

    /**
     * 唯一标识
     */
    private Long id;

    /**
     * 生产线状态
     */
    private ProductionLineStatusEnum status;

    /**
     * 生产线描述
     */
    private String description;

    /**
     * 生产线版本
     */
    private String version;

    /**
     * 业务流程（树形）
     */
    private ProductionNodeInLine nodeTree;

    /**
     * 业务流程内容，主要用作查询
     * key：节点id
     * value：节点对象
     */
    private HashMap<Long, ProductionNodeInLine> lineContent;

    /**
     * 创建一条生产线
     */
    public void create(){
        // todo
        save();
    }
    /**
     * 发布一条生产线
     *  先创建监听关系，然后再修改状态
     */
    public void publish(){
        // todo
    }
    /**
     * 创建然后发布一条生产线
     */
    public void createAndPublish(){
        create();
        publish();
    }

    /**
     * 存储当前生产线
     */
    private void save(){
        ProductionLineGateway productionLineGateway = SpringUtil.getBean(ProductionLineGateway.class);
        productionLineGateway.saveProductionLine(this);
    }

}
