package cn.share.event.bus.server.domain.aggregate;

import cn.hutool.extra.spring.SpringUtil;
import cn.share.event.bus.server.domain.gateway.ProductionLineGateway;
import cn.share.event.bus.server.domain.valueObj.ProductionLineStatusEnum;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * @author gonz
 * @version 1.0
 * @description: 生产线元信息
 * @date 2023/6/20 8:37
 */
@Slf4j
@Getter
public class ProductionLine {

    /**
     * 唯一标识
     */
    private Long id;

    /**
     * 名字
     */
    private String name;

    /**
     * 生产线版本
     */
    private String version;

    /**
     * 生产线状态
     */
    private ProductionLineStatusEnum status;

    /**
     * 生产线描述
     */
    private String description;

    /**
     * 业务流程（树形）
     */
    private ProductionNode nodeTree;

    /**
     * 业务流程内容，主要用作查询
     * key：节点id
     * value：节点对象
     */
    private HashMap<Long, ProductionNode> lineContent;

    public ProductionLine(String name, String version, String description, ProductionNode nodeTree, HashMap<Long, ProductionNode> lineContent) {
        this.name = name;
        this.version = version;
        this.status = ProductionLineStatusEnum.CREATED;
        this.description = description;
        this.nodeTree = nodeTree;
        this.lineContent = lineContent;
    }

    /**
     * 创建一条生产线
     */
    public ProductionLine build() {
        save();
        return this;
    }

    /**
     * 创建一个生产过程
     */
    public void createProductionProcess() {
        // todo
    }

    /**
     * 发布一条生产线
     * 先创建监听关系，然后再修改状态
     */
    public void publish() {
        // todo check
    }

    /**
     * 下线一条生产线
     * 注意，处于下线中状态的流水线不能创建生产过程
     */
    public Boolean offLine() {
        if (!ProductionLineStatusEnum.PUBLISHED.equals(status)) {
            log.info("当前流水线，id:{},未处于已发布状态，不能下线。", this.id);
            return false;
        }
        this.status = ProductionLineStatusEnum.OFF_LINE_ING;
        update();
        return true;
    }

    /**
     * 删除一条生产线
     * 先创建监听关系，然后再修改状态
     */
    public Boolean delete() {
        if (!ProductionLineStatusEnum.OFF_LINE.equals(status)) {
            log.info("当前流水线，id:{},未处于离线状态，不能删除。", this.id);
            return false;
        }
        this.status = ProductionLineStatusEnum.DELETED;
        update();
        return true;
    }

    /**
     * 创建然后发布一条生产线
     */
    public void createAndPublish() {
        build();
        publish();
    }

    /**
     * 存储当前生产线
     */
    private void save() {
        ProductionLineGateway productionLineGateway = SpringUtil.getBean(ProductionLineGateway.class);
        productionLineGateway.saveProductionLine(this);
    }

    /**
     * 存储当前生产线
     */
    private void update() {
        ProductionLineGateway productionLineGateway = SpringUtil.getBean(ProductionLineGateway.class);
        productionLineGateway.updateById(this);
    }

}
