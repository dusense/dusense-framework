package net.dusense.framework.extension.apicrypto.exception;

/** 未配置KEY运行时异常 */
public class KeyNotConfiguredException extends RuntimeException {
    public KeyNotConfiguredException(String message) {
        super(message);
    }
}
