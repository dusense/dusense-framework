package net.dusense.framework.core.context;

import lombok.RequiredArgsConstructor;
import net.dusense.framework.core.context.config.DusenseContextProperties;
import net.dusense.framework.core.context.util.ThreadLocalUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;

import static net.dusense.framework.core.tool.constant.AppConstant.CONTEXT_KEY;

import java.util.function.Function;

/** Dusense servlet 上下文，是DusenseRequestContext的一种实现， 只能作用在ThreadLocal 中，并且跨线程失效 */
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class DusenseServletContext implements DusenseRequestContext {
    private final DusenseContextProperties contextProperties;
    private final DusenseRequestHeadersHandler httpHeadersGetter;

    @Nullable
    @Override
    public String getRequestId() {
        return get(contextProperties.getHeaders().getRequestId());
    }

    @Nullable
    @Override
    public String getAccountId() {
        return get(contextProperties.getHeaders().getAccountId());
    }

    @Nullable
    @Override
    public String getTenantId() {
        return get(contextProperties.getHeaders().getTenantId());
    }

    @Nullable
    @Override
    public String get(String ctxKey) {
        HttpHeaders headers = ThreadLocalUtil.getIfAbsent(CONTEXT_KEY, httpHeadersGetter::get);
        if (headers == null || headers.isEmpty()) {
            return null;
        }
        return headers.getFirst(ctxKey);
    }

    @Nullable
    @Override
    public <T> T get(String ctxKey, Function<String, T> function) {
        String ctxValue = get(ctxKey);
        if (StringUtils.isBlank(ctxValue)) {
            return null;
        }
        return function.apply(ctxKey);
    }
}
