package net.dusense.framework.core.context;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.dusense.framework.core.context.config.DusenseContextProperties;
import net.dusense.framework.core.context.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;

import java.util.Enumeration;
import java.util.List;

/** HttpHeaders 获取器 */
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletHttpHeadersHandler implements DusenseRequestHeadersHandler {
    private final DusenseContextProperties properties;

    @Nullable
    @Override
    public HttpHeaders get() {
        HttpServletRequest request = WebUtil.getRequest();
        if (request == null) {
            return null;
        }
        return get(request);
    }

    @Nullable
    @Override
    public HttpHeaders get(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        List<String> crossHeaders = properties.getCrossHeaders();
        // 传递请求头
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            List<String> allowed = properties.getHeaders().getAllowed();
            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
                // 只支持配置的 header
                if (crossHeaders.contains(key) || allowed.contains(key)) {
                    String values = request.getHeader(key);
                    // header value 不为空的 传递
                    if (StringUtils.isNotBlank(values)) {
                        headers.add(key, values);
                    }
                }
            }
        }
        return headers;
    }
}
