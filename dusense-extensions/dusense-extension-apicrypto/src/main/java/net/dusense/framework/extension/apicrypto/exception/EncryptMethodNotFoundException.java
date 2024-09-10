package net.dusense.framework.extension.apicrypto.exception;

/** 加密方式未找到或未定义异常 */
public class EncryptMethodNotFoundException extends RuntimeException {
    public EncryptMethodNotFoundException() {
        super("Encryption method is not defined. (加密方式未定义)");
    }
}
