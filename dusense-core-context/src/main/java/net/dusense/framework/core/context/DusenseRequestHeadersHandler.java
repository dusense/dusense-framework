package net.dusense.framework.core.context;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;

/**
 * HttpHeaders 获取器，用于跨服务和线程的传递，
 *
 * <p>暂时不支持 webflux。
 */
public interface DusenseRequestHeadersHandler {

    /**
     * 获取 HttpHeaders
     *
     * @return HttpHeaders
     */
    @Nullable
    HttpHeaders get();

    /**
     * 获取 HttpHeaders
     *
     * @param request 请求
     * @return HttpHeaders
     */
    @Nullable
    HttpHeaders get(HttpServletRequest request);
}
