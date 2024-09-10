package net.dusense.framework.extension.apicrypto.annotation.encrypt;

import net.dusense.framework.extension.apicrypto.enums.CryptoType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * aes 加密
 *
 * @author licoy.cn, L.cm
 * @see ApiEncrypt
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiEncrypt(CryptoType.AES)
public @interface ApiEncryptAes {

    /**
     * Alias for {@link ApiEncrypt#secretKey()}.
     *
     * @return {String}
     */
    @AliasFor(annotation = ApiEncrypt.class)
    String secretKey() default "";
}
