package net.dusense.framework.core.launcher.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * <p>web response 支持类</p>
 *
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2021/09/06
 */
@Slf4j
public class WebResponseSupport {
    public ResponseEntity<String> getSucceedStringResp() {
        return getStringResp("Successfully.", HttpStatus.OK);
    }

    public ResponseEntity<String> getStringResp(String body, HttpStatus statusCode) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "text/plain;charset=utf-8");
        return new ResponseEntity<String>(body, responseHeaders, statusCode);
    }

    public <T> ResponseEntity<T> getJSONResp(T body, HttpStatus statusCode) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json;charset=utf-8");
        return new ResponseEntity<T>(body, responseHeaders, statusCode);
    }

    protected final boolean isPost(HttpServletRequest request) {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        return false;
    }

    protected boolean isAjaxRequest(HttpServletRequest request) {
        String xmlRequest = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equalsIgnoreCase(xmlRequest) ? true : false;
    }

    protected void handleErr(Exception ex, HttpServletRequest request) {
        handleErr(ex.getMessage(), ex, request);
    }

    /**
     * Discription:[处理异常方法]
     *
     * @param ex
     * @author:[Fan Yang]
     */
    protected final void handleErr(String message, Exception ex, HttpServletRequest request) {
        try {
            request.setAttribute("javax.servlet.error.exception", ex);
            request.setAttribute("ERROR_MSG", message);
            // response.setStatus(response.SC_INTERNAL_SERVER_ERROR, message);
            // response.sendError(response.SC_INTERNAL_SERVER_ERROR, message);
            log.error(message, ex);
        } catch (Exception e) {

        } finally {
            // throw new ApplicationException(ex);
        }
    }
}
