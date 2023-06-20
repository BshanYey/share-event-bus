package cn.share.event.bus.server.domain.aggregate;

import cn.share.event.bus.server.domain.valueObj.ProductionNodeInput;
import cn.share.event.bus.server.domain.valueObj.ProductionNodeOutput;
import cn.share.event.bus.server.domain.valueObj.ProductionNodeStatusEnum;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gonz
 * @version 1.0
 * @description: 生产节点聚合根
 * @date 2023/6/20 8:37
 */
@Slf4j
@NoArgsConstructor
public class ProductionNode {

    /**
     * 唯一标识
     */
    private Long id;

    /**
     * 节点编号
     */
    private String no;

    /**
     * 生产节点的状态
     */
    private ProductionNodeStatusEnum status;

    /**
     * 节点描述
     */
    private String description;

    /**
     * 节点来源
     */
    private String source;

    /**
     * 节点版本
     */
    private String version;

    /**
     * 节点入口集合
     */
    private ProductionNodeInput inputSet;

    /**
     * 节点出口集合
     */
    private ProductionNodeOutput outputSet;



    /**
     * 注册一个生产节点
     */
    public void register(){
        // todo
    }

    /**
     * 上线一个生产节点
     */
    public void onLine(){
        this.status = ProductionNodeStatusEnum.ON_LINE;
        save();
    }

    /**
     * 下线一个生产节点
     */
    public void offLine(){
        this.status = ProductionNodeStatusEnum.OFF_LINE;
        save();
    }

    /**
     * 保存当前节点
     */
    private void save(){
        // todo
    }

}
