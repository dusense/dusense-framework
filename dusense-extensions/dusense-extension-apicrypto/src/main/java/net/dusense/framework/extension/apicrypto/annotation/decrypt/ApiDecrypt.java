package net.dusense.framework.extension.apicrypto.annotation.decrypt;

import net.dusense.framework.extension.apicrypto.enums.CryptoType;

import java.lang.annotation.*;

/** 解密含有{@link org.springframework.web.bind.annotation.RequestBody}注解的参数请求数据，可用于整个控制类或者某个控制器上 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ApiDecrypt {

    /**
     * 解密类型
     *
     * @return 类型
     */
    CryptoType value();

    /**
     * 私钥，用于某些需要单独配置私钥的方法，没有时读取全局配置的私钥
     *
     * @return 私钥
     */
    String secretKey() default "";
}
