package net.dusense.framework.core.cloud.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.dusense.framework.core.tool.jackson.JacksonUtils;
import net.dusense.framework.core.web.R;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/** Sentinel统一限流策略 */
public class DusenseBlockExceptionHandler implements BlockExceptionHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            String resourceName,
            BlockException e)
            throws Exception {
        // Return 429 (Too Many Requests) by default.
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().print(JacksonUtils.toJsonString(R.fail(e.getMessage())));
    }
}
