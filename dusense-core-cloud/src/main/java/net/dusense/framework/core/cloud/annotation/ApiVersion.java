package net.dusense.framework.core.cloud.annotation;

import java.lang.annotation.*;

/** header 版本 处理 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ApiVersion {

    String value() default "";
}
