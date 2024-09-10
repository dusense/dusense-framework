package net.dusense.framework.core.cloud.feign;

import com.fasterxml.jackson.databind.JsonNode;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dusense.framework.core.tool.jackson.JacksonUtils;
import net.dusense.framework.core.web.R;
import net.dusense.framework.core.web.RespCode;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Dusense Fegin fallBack 代理处理
 *
 * @author [ saily ]
 * @email sailyfirm@gmail.com
 * @version V3.0
 * @param <T>
 * @date 2022/08/11
 */
@Slf4j
@AllArgsConstructor
public class DusenseFeignFallback<T> implements MethodInterceptor {
    private final Class<T> targetType;
    private final String targetName;
    private final Throwable cause;
    private static final String CODE = "code";

    @Nullable
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy)
            throws Throwable {
        String errorMessage = cause.getMessage();
        log.error(
                "DusenseFeignFallback:[{}.{}] serviceId:[{}] message:[{}]",
                targetType.getName(),
                method.getName(),
                targetName,
                errorMessage);
        Class<?> returnType = method.getReturnType();
        // 集合类型反馈空集合
        if (List.class == returnType || Collection.class == returnType) {
            return Collections.emptyList();
        }
        if (Set.class == returnType) {
            return Collections.emptySet();
        }
        if (Map.class == returnType) {
            return Collections.emptyMap();
        }
        // 暂时不支持 flux，rx，异步等，返回值不是 R，直接返回 null。
        if (R.class != returnType) {
            return null;
        }
        // 非 FeignException
        if (!(cause instanceof FeignException)) {
            return R.fail(RespCode.INTERNAL_SERVER_ERROR, errorMessage);
        }
        FeignException exception = (FeignException) cause;
        byte[] content = exception.content();
        // 如果返回的数据为空
        if (ObjectUtils.isEmpty(content)) {
            return R.fail(RespCode.INTERNAL_SERVER_ERROR, errorMessage);
        }
        // 转换成 jsonNode 读取，因为直接转换，可能 对方放回的并 不是 R 的格式。
        JsonNode resultNode = JacksonUtils.readTree(content);
        // 判断是否 R 格式 返回体
        if (resultNode.has(CODE)) {
            return JacksonUtils.getInstance().convertValue(resultNode, R.class);
        }
        return R.fail(resultNode.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DusenseFeignFallback<?> that = (DusenseFeignFallback<?>) o;
        return targetType.equals(that.targetType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetType);
    }
}
