package net.dusense.framework.extension.apicrypto.annotation.decrypt;

import net.dusense.framework.extension.apicrypto.enums.CryptoType;

import java.lang.annotation.*;

/**
 * rsa 解密
 *
 * @author licoy.cn
 * @see ApiDecrypt
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiDecrypt(CryptoType.RSA)
public @interface ApiDecryptRsa {}
