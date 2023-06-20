package cn.share.event.bus.server.application.dto;

import cn.share.event.bus.server.domain.valueObj.ProductionNodeInput;

import java.util.List;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-20 18:35:58
 * @describe class responsibility
 */
public class ProductNodeDto {
    private String serviceName;

    private String name;

    private String version;

    private String desc;

    private List<ProductionNodeInput> input;
}
