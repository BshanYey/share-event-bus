package cn.share.event.bus.client.scan;

import cn.share.event.bus.client.annotation.Register;
import cn.share.event.bus.client.register.RegisterNodeProcessor;
import javax.annotation.Resource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-26 14:37:00
 * @describe class responsibility
 */
@Component
public class RegisterPostProcessor implements BeanPostProcessor {

    @Resource
    RegisterNodeProcessor nodeProcessor;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        parseAndRegister(bean);
        return bean;
    }

    private void parseAndRegister(Object bean) {
        Class<?> clazz = bean.getClass();
        // 扫描@Register注册类
        if (!clazz.isAnnotationPresent(Register.class)) {
            return;
        }
        nodeProcessor.register(bean);
    }
}
