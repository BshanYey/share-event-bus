package cn.share.event.bus.client.entity.param;

import lombok.Data;

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

    private String input;

    private String output;

    private boolean whetherNormal;
}
