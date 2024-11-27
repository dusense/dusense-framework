package net.dusense.framework.extension.apicrypto.util;

import net.dusense.framework.core.tool.utils.AesUtil;
import net.dusense.framework.core.tool.utils.ClassUtil;
import net.dusense.framework.core.tool.utils.DesUtil;
import net.dusense.framework.core.tool.utils.RsaUtil;
import net.dusense.framework.extension.apicrypto.annotation.decrypt.ApiDecrypt;
import net.dusense.framework.extension.apicrypto.annotation.encrypt.ApiEncrypt;
import net.dusense.framework.extension.apicrypto.bean.CryptoInfoBean;
import net.dusense.framework.extension.apicrypto.config.ApiCryptoProperties;
import net.dusense.framework.extension.apicrypto.enums.CryptoType;
import net.dusense.framework.extension.apicrypto.exception.EncryptBodyFailException;
import net.dusense.framework.extension.apicrypto.exception.EncryptMethodNotFoundException;
import net.dusense.framework.extension.apicrypto.exception.KeyNotConfiguredException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * 加密工具类
 *
 * @author [ saily ]
 * @email sailyfirm@gmail.com
 * @version V3.0
 * @date 2021/09/06
 */
public class ApiCryptoUtil {

    /**
     * 获取方法控制器上的加密注解信息
     *
     * @param methodParameter 控制器方法
     * @return 加密注解信息
     */
    @Nullable
    public static CryptoInfoBean getEncryptInfo(MethodParameter methodParameter) {
        ApiEncrypt encryptBody =
                ClassUtil.getAnnotation(methodParameter.getMethod(), ApiEncrypt.class);
        if (encryptBody == null) {
            return null;
        }
        return new CryptoInfoBean(encryptBody.value(), encryptBody.secretKey());
    }

    /**
     * 获取方法控制器上的解密注解信息
     *
     * @param methodParameter 控制器方法
     * @return 加密注解信息
     */
    @Nullable
    public static CryptoInfoBean getDecryptInfo(MethodParameter methodParameter) {
        ApiDecrypt decryptBody =
                ClassUtil.getAnnotation(methodParameter.getMethod(), ApiDecrypt.class);
        if (decryptBody == null) {
            return null;
        }
        return new CryptoInfoBean(decryptBody.value(), decryptBody.secretKey());
    }

    /**
     * 选择加密方式并进行加密
     *
     * @param jsonData json 数据
     * @param infoBean 加密信息
     * @return 加密结果
     */
    public static String encryptData(
            ApiCryptoProperties properties, byte[] jsonData, CryptoInfoBean infoBean) {
        CryptoType type = infoBean.getType();
        if (type == null) {
            throw new EncryptMethodNotFoundException();
        }
        String secretKey = infoBean.getSecretKey();
        if (type == CryptoType.DES) {
            secretKey = ApiCryptoUtil.checkSecretKey(properties.getDesKey(), secretKey, "DES");
            return DesUtil.encryptToBase64(jsonData, secretKey);
        }
        if (type == CryptoType.AES) {
            secretKey = ApiCryptoUtil.checkSecretKey(properties.getAesKey(), secretKey, "AES");
            return AesUtil.encryptToBase64(jsonData, secretKey);
        }
        if (type == CryptoType.RSA) {
            String privateKey = Objects.requireNonNull(properties.getRsaPrivateKey());
            return RsaUtil.encryptByPrivateKeyToBase64(privateKey, jsonData);
        }
        throw new EncryptBodyFailException();
    }

    /**
     * 选择加密方式并进行解密
     *
     * @param bodyData byte array
     * @param infoBean 加密信息
     * @return 解密结果
     */
    public static byte[] decryptData(
            ApiCryptoProperties properties, byte[] bodyData, CryptoInfoBean infoBean) {
        CryptoType type = infoBean.getType();
        if (type == null) {
            throw new EncryptMethodNotFoundException();
        }
        String secretKey = infoBean.getSecretKey();
        if (type == CryptoType.AES) {
            secretKey = ApiCryptoUtil.checkSecretKey(properties.getAesKey(), secretKey, "AES");
            return AesUtil.decryptFormBase64(bodyData, secretKey);
        }
        if (type == CryptoType.DES) {
            secretKey = ApiCryptoUtil.checkSecretKey(properties.getDesKey(), secretKey, "DES");
            return DesUtil.decryptFormBase64(bodyData, secretKey);
        }
        if (type == CryptoType.RSA) {
            String privateKey = Objects.requireNonNull(properties.getRsaPrivateKey());
            return RsaUtil.decryptFromBase64(privateKey, bodyData);
        }
        throw new EncryptMethodNotFoundException();
    }

    /**
     * 检验私钥
     *
     * @param k1 配置的私钥
     * @param k2 注解上的私钥
     * @param keyName key名称
     * @return 私钥
     */
    private static String checkSecretKey(String k1, String k2, String keyName) {
        if (StringUtils.isBlank(k1) && StringUtils.isBlank(k2)) {
            throw new KeyNotConfiguredException(
                    String.format("%s key is not configured (未配置%s)", keyName, keyName));
        }
        return StringUtils.isBlank(k2) ? k1 : k2;
    }
}
