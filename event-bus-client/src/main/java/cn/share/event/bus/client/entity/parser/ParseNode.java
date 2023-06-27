package cn.share.event.bus.client.entity.parser;

import cn.share.event.bus.client.annotation.EventRegister;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-27 14:17:28
 * @describe class responsibility
 */
@Data
public class ParseNode {
    private Method method;
    private EventRegister eventRegister;
}
