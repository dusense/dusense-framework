package net.dusense.framework.extension.apicrypto.annotation.decrypt;

import net.dusense.framework.extension.apicrypto.enums.CryptoType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * aes 解密
 *
 * @author licoy.cn, L.cm
 * @see ApiDecrypt
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiDecrypt(CryptoType.AES)
public @interface ApiDecryptAes {

    /**
     * Alias for {@link ApiDecrypt#secretKey()}.
     *
     * @return {String}
     */
    @AliasFor(annotation = ApiDecrypt.class)
    String secretKey() default "";
}
