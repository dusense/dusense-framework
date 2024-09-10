package net.dusense.framework.extension.apicrypto.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>api 签名配置类</p>
 *
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2021/09/06
 */
@Getter
@Setter
@ConfigurationProperties(ApiCryptoProperties.PREFIX)
public class ApiCryptoProperties {
    /**
     * 前缀
     */
    public static final String PREFIX = "dusense.api.crypto";

    /**
     * 是否开启 api 签名
     */
    private Boolean enabled = Boolean.FALSE;

    /**
     * url的参数签名，传递的参数名。例如：/user?data=签名后的数据
     */
    private String paramName = "data";

    /**
     * aes 密钥
     */
    private String aesKey;

    /**
     * des 密钥
     */
    private String desKey;

    /**
     * rsa 私钥
     */
    private String rsaPrivateKey;
}
