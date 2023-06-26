package cn.share.event.bus.client.entity;

import lombok.Data;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-26 15:05:32
 * @describe class responsibility
 */
@Data
public class Param {
    private String param;
    private String paramType;
    private Integer paramOrder;
    private Boolean javaClass;
}
