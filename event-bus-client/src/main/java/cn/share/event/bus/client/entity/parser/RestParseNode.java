package cn.share.event.bus.client.entity.parser;

import lombok.Data;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-27 14:18:13
 * @describe class responsibility
 */
@Data
public class RestParseNode extends ParseNode {
    private String api;
}
