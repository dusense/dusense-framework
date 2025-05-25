package net.dusense.framework.extension.apicrypto.bean;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dusense.framework.extension.apicrypto.enums.CryptoType;

/** 加密注解信息 */
@Getter
@RequiredArgsConstructor
public class CryptoInfoBean {

    /** 加密类型 */
    private final CryptoType type;

    /** 私钥 */
    private final String secretKey;
}
