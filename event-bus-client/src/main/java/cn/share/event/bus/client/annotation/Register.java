package cn.share.event.bus.client.annotation;


import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 14:24:46
 * @describe class responsibility
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Component
public @interface Register {
}
