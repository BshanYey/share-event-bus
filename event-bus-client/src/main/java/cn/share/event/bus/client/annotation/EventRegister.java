package cn.share.event.bus.client.annotation;

import cn.share.event.bus.client.entity.param.None;
import cn.share.event.bus.client.enums.RegisterType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-21 14:24:46
 * @describe class responsibility
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface EventRegister {
    /**
     *  功能名称
     *
     * @return 功能名称
     */
    String funcName();

    /**
     *  MQ 注册时使用
     *
     * @return 主题
     */
    String topic() default "";

    /**
     *  功能节点注册版本号
     *
     * @return 版本号
     */
    String version() default "1.0";

    /**
     *  功能描述
     *
     * @return 描述信息
     */
    String desc();

    /**
     *   请求参数类型
     *
     * @return Class
     */
    Class<?> requestType() default None.class;

    /**
     *  想要参数类型
     *
     * @return Class
     */
    Class<?> responseType() default None.class;

    /**
     *  注册类型
     *
     * @return 默认为REST
     */
    RegisterType registerType() default RegisterType.REST;
}
