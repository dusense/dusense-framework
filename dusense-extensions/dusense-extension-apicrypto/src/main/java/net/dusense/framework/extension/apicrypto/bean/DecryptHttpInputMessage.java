package net.dusense.framework.extension.apicrypto.bean;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.InputStream;

/** 解密信息输入流 */
@Getter
@RequiredArgsConstructor
public class DecryptHttpInputMessage implements HttpInputMessage {
    private final InputStream body;
    private final HttpHeaders headers;
}
