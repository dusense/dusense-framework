package net.dusense.framework.core.tool.utils;

import net.dusense.framework.core.exception.Exceptions;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Objects;

/** DES加解密处理工具 */
public class DesUtil {
    /** 数字签名，密钥算法 */
    public static final String DES_ALGORITHM = "DES";

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * 生成 des 密钥
     *
     * @return 密钥
     */
    public static String genDesKey() {
        // return StringUtils.random(16);
        return null;
    }

    /**
     * DES加密
     *
     * @param data byte array
     * @param password 密钥
     * @return des hex
     */
    public static String encryptToHex(byte[] data, String password) {
        return Hex.encodeHexString(encrypt(data, password));
    }

    /**
     * DES加密
     *
     * @param data 字符串内容
     * @param password 密钥
     * @return des hex
     */
    @Nullable
    public static String encryptToHex(@Nullable String data, String password) {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        return encryptToHex(dataBytes, password);
    }

    /**
     * DES解密
     *
     * @param data 字符串内容
     * @param password 密钥
     * @return des context
     */
    @Nullable
    public static String decryptFormHex(@Nullable String data, String password) {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        byte[] hexBytes = new byte[0];
        try {
            hexBytes = Hex.decodeHex(data);
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
        return new String(decrypt(hexBytes, password), StandardCharsets.UTF_8);
    }

    /**
     * DES加密
     *
     * @param data byte array
     * @param password 密钥
     * @return des hex
     */
    public static String encryptToBase64(byte[] data, String password) {
        return Base64.encodeBase64String(encrypt(data, password));
    }

    /**
     * DES加密
     *
     * @param data 字符串内容
     * @param password 密钥
     * @return des hex
     */
    @Nullable
    public static String encryptToBase64(@Nullable String data, String password) {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        return encryptToBase64(dataBytes, password);
    }

    /**
     * DES解密
     *
     * @param data 字符串内容
     * @param password 密钥
     * @return des context
     */
    public static byte[] decryptFormBase64(byte[] data, String password) {
        byte[] dataBytes = Base64.decodeBase64(data);
        return decrypt(dataBytes, password);
    }

    /**
     * DES解密
     *
     * @param data 字符串内容
     * @param password 密钥
     * @return des context
     */
    @Nullable
    public static String decryptFormBase64(@Nullable String data, String password) {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        byte[] dataBytes = Base64.decodeBase64(data);
        return new String(decrypt(dataBytes, password), StandardCharsets.UTF_8);
    }

    /**
     * DES加密
     *
     * @param data 内容
     * @param desKey 密钥
     * @return byte array
     */
    public static byte[] encrypt(byte[] data, byte[] desKey) {
        return des(data, desKey, Cipher.ENCRYPT_MODE);
    }

    /**
     * DES加密
     *
     * @param data 内容
     * @param desKey 密钥
     * @return byte array
     */
    public static byte[] encrypt(byte[] data, String desKey) {
        return encrypt(data, Objects.requireNonNull(desKey).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * DES解密
     *
     * @param data 内容
     * @param desKey 密钥
     * @return byte array
     */
    public static byte[] decrypt(byte[] data, byte[] desKey) {
        return des(data, desKey, Cipher.DECRYPT_MODE);
    }

    /**
     * DES解密
     *
     * @param data 内容
     * @param desKey 密钥
     * @return byte array
     */
    public static byte[] decrypt(byte[] data, String desKey) {
        return decrypt(data, Objects.requireNonNull(desKey).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * DES加密/解密公共方法
     *
     * @param data byte数组
     * @param desKey 密钥
     * @param mode 加密：{@link Cipher#ENCRYPT_MODE}，解密：{@link Cipher#DECRYPT_MODE}
     * @return des
     */
    private static byte[] des(byte[] data, byte[] desKey, int mode) {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
            DESKeySpec desKeySpec = new DESKeySpec(desKey);
            cipher.init(mode, keyFactory.generateSecret(desKeySpec), SECURE_RANDOM);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }
}
