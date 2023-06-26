package cn.share.event.bus.client.entity;

import lombok.Data;

import java.util.Map;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-26 10:55:57
 * @describe class responsibility
 */
@Data
public class Node {
    private String registerType;

    private String nodeName;

    private String version;

    private String desc;

    private Map<String, Object> input;

    private Map<String, Object> output;

    private boolean whetherNormal;
}
