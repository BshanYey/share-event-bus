package cn.share.event.bus.client.event;

import cn.share.event.bus.client.register.nodeparse.RegisterHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-29 18:45:29
 * @describe class responsibility
 */
@Slf4j
@Component
public class RegisterEventListener implements ApplicationListener<RegisterEvent> {
    @Override
    public void onApplicationEvent(RegisterEvent event) {
        log.info("注册事件触发.....");
        RegisterHandler.register();
    }
}
