package net.dusense.framework.core.tool.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * ${信任所有 host name}
 *
 * @author [ saily ]
 * @email sailyfirm@gmail.com
 * @version V3.0
 * @date 2022/03/01
 */
public class TrustAllHostNames implements HostnameVerifier {
    public static final TrustAllHostNames INSTANCE = new TrustAllHostNames();

    @Override
    public boolean verify(String s, SSLSession sslSession) {
        return true;
    }
}
