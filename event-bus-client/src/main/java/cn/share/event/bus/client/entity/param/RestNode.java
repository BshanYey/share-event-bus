package cn.share.event.bus.client.entity.param;

import lombok.Data;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-26 11:01:57
 * @describe class responsibility
 */
@Data
public class RestNode extends Node {
    private String uri;

    private String httpMethod;

    private boolean includePathValue;
}
