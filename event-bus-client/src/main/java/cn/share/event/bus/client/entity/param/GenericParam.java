package cn.share.event.bus.client.entity.param;

import lombok.Data;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-27 11:43:03
 * @describe 主要用于Map<Object, Object> 参数中的key value存储,  不存储 jdk包中的类.
 */
@Data
public class GenericParam {
    private String key;

    private Boolean isJavaClass;

    private Boolean isKey;

    private String value;
}
