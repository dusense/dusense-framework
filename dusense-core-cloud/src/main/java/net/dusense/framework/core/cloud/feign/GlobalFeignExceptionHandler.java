package net.dusense.framework.core.cloud.feign;

import feign.FeignException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2024/03/16
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalFeignExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public String handleFeignException(FeignException ex) {
        // 处理异常
        return "error";
    }
}
