package cn.share.event.bus.client.entity;

import lombok.Data;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-26 10:56:35
 * @describe class responsibility
 */
@Data
public class EventNode extends Node {
    private String topic;
}
