package net.dusense.framework.core.launcher.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.dusense.framework.core.context.support.UserContextConstants;
import net.dusense.framework.core.context.util.WebUtil;
import net.dusense.framework.core.tool.jackson.JacksonUtils;
import net.dusense.framework.core.web.R;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.net.URI;
import java.util.List;

/**
 * Description: [mvc 统一返回 & 区分调用来源]
 *
 * @version $Revision$
 * @author:[Fan Yang]
 */
@ControllerAdvice
@Slf4j
@ConditionalOnClass(ResponseBodyAdvice.class)
public class GlobalDusenseResponseHandler implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        if (request.getURI().getPath().startsWith("/v2")
                || request.getURI().getPath().startsWith("/swagger")) {
            return body;
        }

        // 区分调度行为
        List<String> feginHead =
                request.getHeaders().get(UserContextConstants.FEIGN_HEADER_REQFLAG);
        if (feginHead != null && "1".equals(feginHead.get(0))) {
            // 为微服务feign调度
            return body;
        }

        if (body == null) {
            // 抛出404 not found?
        }
        // 针对string 类型返回  string message Convert
        // 日志格式  ip username url type
        HttpServletRequest servletRequest =
                ((ServletServerHttpRequest) request).getServletRequest();
        String address = WebUtil.getIP(servletRequest);
        URI host = request.getURI();
        int status = ((ServletServerHttpResponse) response).getServletResponse().getStatus();

        //        String userID = UserContextHolder.getUserID();
        //        logger.info(address + " " + userID + " " + host + " " + status);

        if (body instanceof R) {
            return body;
        } else if (body instanceof ExceptionRespEntity) {
            // 2次包装ExceptionRespEntity
            return R.fail(((ExceptionRespEntity) body).getMsg())
                    .setStatus(
                            ((ServletServerHttpResponse) response)
                                    .getServletResponse()
                                    .getStatus());
        } else if (String.class.isAssignableFrom(returnType.getParameterType())) {
            // 此处为了修补为String 的返回内容
            return JacksonUtils.toJsonString(R.ok(body));
        } else if (ResponseEntity.class.isAssignableFrom(returnType.getParameterType())) {
            return body;
        }

        // remove the feign-req ?不必要的暴露接口
        return R.ok(body);
    }
}
