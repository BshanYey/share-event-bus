package cn.share.event.bus.server.domain.aggregate;

import cn.hutool.extra.spring.SpringUtil;
import cn.share.event.bus.server.domain.gateway.ProductionNodeGateway;
import cn.share.event.bus.server.domain.valueObj.ProductionNodeInput;
import cn.share.event.bus.server.domain.valueObj.ProductionNodeOutput;
import cn.share.event.bus.server.domain.valueObj.ProductionNodeStatusEnum;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gonz
 * @version 1.0
 * @description: 生产节点聚合根
 * @date 2023/6/20 8:37
 */
@Slf4j
@Getter
//@NoArgsConstructor
public class ProductionNode {

    /**
     * 唯一标识
     */
    private Long id;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点所属业务服务
     */
    private String serviceName;

    /**
     * 节点版本
     */
    private String version;

    /**
     * 节点描述
     */
    private String description;

    /**
     * 生产节点的状态
     */
    private ProductionNodeStatusEnum status;

    /**
     * 节点入口集合
     */
    private ProductionNodeInput inputSet;

    /**
     * 节点出口集合
     */
    private ProductionNodeOutput outputSet;

    public ProductionNode(String name, String serviceName, String version, String description, ProductionNodeInput inputSet, ProductionNodeOutput outputSet) {
        this.name = name;
        this.serviceName = serviceName;
        this.version = version;
        this.description = description;
        this.status = ProductionNodeStatusEnum.ON_LINE;
        this.inputSet = inputSet;
        this.outputSet = outputSet;
    }

    /**
     * 注册一个生产节点
     */
    public void register() throws Exception {
        if(!ProductionNodeStatusEnum.ON_LINE.equals(status)){
            log.info("当前生产节点未处于在线状态不能注册，信息：{}", new Gson().toJson(this));
            throw new Exception("当前生产节点未处于在线状态不能注册");
        }
        save();
    }

    /**
     * 上线一个生产节点
     */
    public void onLine(){
        this.status = ProductionNodeStatusEnum.ON_LINE;
        update();
    }

    /**
     * 下线一个生产节点
     */
    public void offLine(){
        this.status = ProductionNodeStatusEnum.OFF_LINE;
        update();
    }

    /**
     * 保存当前节点
     */
    private void save(){
        ProductionNodeGateway nodeGateway = SpringUtil.getBean(ProductionNodeGateway.class);
        this.id = nodeGateway.save(this);
    }

    /**
     * 更新当前节点
     */
    private void update(){
        ProductionNodeGateway nodeGateway = SpringUtil.getBean(ProductionNodeGateway.class);
        nodeGateway.updateById(this);
    }

}
