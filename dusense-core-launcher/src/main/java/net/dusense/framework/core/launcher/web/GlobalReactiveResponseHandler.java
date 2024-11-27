package net.dusense.framework.core.launcher.web;

import lombok.extern.slf4j.Slf4j;
import net.dusense.framework.core.web.R;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

/**
 * 全局响应式返回结果处理
 *
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2021/09/06
 */
@Slf4j
public class GlobalReactiveResponseHandler extends ResponseBodyResultHandler {

    private static MethodParameter METHOD_PARAMETER_MONO_COMMON_RESULT;

    static {
        try {
            // 获得 METHOD_PARAMETER_MONO_COMMON_RESULT 。其中 -1 表示 `#methodForParams()` 方法的返回值
            METHOD_PARAMETER_MONO_COMMON_RESULT =
                    new MethodParameter(
                            GlobalReactiveResponseHandler.class.getDeclaredMethod(
                                    "methodForParams"),
                            -1);
        } catch (NoSuchMethodException e) {
            log.error("[static][获取 METHOD_PARAMETER_MONO_COMMON_RESULT 时，找不都方法");
            throw new RuntimeException(e);
        }
    }

    public GlobalReactiveResponseHandler(
            List<HttpMessageWriter<?>> writers, RequestedContentTypeResolver resolver) {
        super(writers, resolver);
    }

    public GlobalReactiveResponseHandler(
            List<HttpMessageWriter<?>> writers,
            RequestedContentTypeResolver resolver,
            ReactiveAdapterRegistry registry) {
        super(writers, resolver, registry);
    }

    @Override
    public boolean supports(HandlerResult result) {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
        Object returnValue = result.getReturnValue();
        Object body;
        // <1.1>  处理返回结果为 Mono 的情况
        if (returnValue instanceof Mono) {
            body =
                    ((Mono<Object>) result.getReturnValue())
                            .map(
                                    (Function<Object, Object>)
                                            GlobalReactiveResponseHandler::wrapCommonResult)
                            .defaultIfEmpty(R.ok());
            //  <1.2> 处理返回结果为 Flux 的情况
        } else if (returnValue instanceof Flux<?>) {
            body =
                    ((Flux<Object>) result.getReturnValue())
                            .collectList()
                            .map(
                                    (Function<Object, Object>)
                                            GlobalReactiveResponseHandler::wrapCommonResult)
                            .defaultIfEmpty(R.ok());
            //  <1.3> 处理结果为其它类型
        } else {
            body = wrapCommonResult(returnValue);
        }
        return writeBody(body, METHOD_PARAMETER_MONO_COMMON_RESULT, exchange);
    }

    private static Mono<R> methodForParams() {
        return null;
    }

    private static R<?> wrapCommonResult(Object body) {
        // 如果已经是 CommonResult 类型，则直接返回
        if (body instanceof R) {
            return (R<?>) body;
        }
        // 如果不是，则包装成 CommonResult 类型
        return R.ok(body);
    }
}
