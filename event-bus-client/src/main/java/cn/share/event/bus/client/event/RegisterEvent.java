package cn.share.event.bus.client.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-29 18:44:45
 * @describe class responsibility
 */
public class RegisterEvent extends ApplicationEvent {
    public RegisterEvent(Object source) {
        super(source);
    }
}
