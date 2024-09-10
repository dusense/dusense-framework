package net.dusense.framework.extension.apicrypto.annotation.crypto;

import net.dusense.framework.extension.apicrypto.annotation.decrypt.ApiDecrypt;
import net.dusense.framework.extension.apicrypto.annotation.encrypt.ApiEncrypt;
import net.dusense.framework.extension.apicrypto.enums.CryptoType;

import java.lang.annotation.*;

/** RSA加密解密含有{@link org.springframework.web.bind.annotation.RequestBody}注解的参数请求数据 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ApiEncrypt(CryptoType.RSA)
@ApiDecrypt(CryptoType.RSA)
public @interface ApiCryptoRsa {}
