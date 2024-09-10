package net.dusense.framework.extension.apicrypto.annotation.encrypt;

import net.dusense.framework.extension.apicrypto.enums.CryptoType;

import java.lang.annotation.*;

/**
 * rsa 加密
 *
 * @author licoy.cn, L.cm
 * @see ApiEncrypt
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiEncrypt(CryptoType.RSA)
public @interface ApiEncryptRsa {}
