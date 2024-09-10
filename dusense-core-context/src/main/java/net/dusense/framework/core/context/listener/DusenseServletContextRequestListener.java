package net.dusense.framework.core.context.listener;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.dusense.framework.core.context.DusenseRequestHeadersHandler;
import net.dusense.framework.core.context.config.DusenseContextProperties;
import net.dusense.framework.core.context.util.ThreadLocalUtil;
import net.dusense.framework.core.tool.constant.AppConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;

/** Servlet 请求监听器 */
@RequiredArgsConstructor
public class DusenseServletContextRequestListener implements ServletRequestListener {
    private final DusenseContextProperties contextProperties;
    private final DusenseRequestHeadersHandler requestHeadersHandler;

    @Override
    public void requestInitialized(ServletRequestEvent event) {
        HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
        // MDC 获取透传的 变量
        DusenseContextProperties.Headers headers = contextProperties.getHeaders();
        String requestId = request.getHeader(headers.getRequestId());
        if (StringUtils.isNotBlank(requestId)) {
            MDC.put(AppConstant.MDC_REQUEST_ID_KEY, requestId);
        }
        String accountId = request.getHeader(headers.getAccountId());
        if (StringUtils.isNotBlank(accountId)) {
            MDC.put(AppConstant.MDC_ACCOUNT_ID_KEY, accountId);
        }
        String tenantId = request.getHeader(headers.getTenantId());
        if (StringUtils.isNotBlank(tenantId)) {
            MDC.put(AppConstant.MDC_TENANT_ID_KEY, tenantId);
        }
        // 处理 context，直接传递 request，因为 spring 中的尚未初始化完成
        HttpHeaders httpHeaders = requestHeadersHandler.get(request);
        ThreadLocalUtil.put(AppConstant.CONTEXT_KEY, httpHeaders);
    }

    @Override
    public void requestDestroyed(ServletRequestEvent event) {
        // 会话销毁时，清除上下文
        ThreadLocalUtil.clear();
        // 会话销毁时，清除 mdc
        MDC.remove(AppConstant.MDC_REQUEST_ID_KEY);
        MDC.remove(AppConstant.MDC_ACCOUNT_ID_KEY);
        MDC.remove(AppConstant.MDC_TENANT_ID_KEY);
    }
}
