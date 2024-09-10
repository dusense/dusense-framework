package net.dusense.framework.core.context.support;

import net.dusense.framework.core.context.DusenseThreadLocal;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: [Web 应用 线程上下文 Holder]
 *
 * @author [ Fan Yang ]
 * @version $1.0$
 */
public class WebRequestContextHolder {

    /** 通过Thread 隔离对应的请求参数* */
    private static final DusenseThreadLocal<Map> requestParamHolder = new DusenseThreadLocal<Map>();

    public static Map getRequestParam() {
        return requestParamHolder.get();
    }

    public static void setRequestParam(Map params) {
        requestParamHolder.set(params);
    }

    public static Object getContextParam(String key) {
        Map contextParams = getRequestParam();
        Object result = null;
        if (contextParams != null && key != null) {
            result = contextParams.get(key);
        }

        return result;
    }

    public static void setContextParam(String key, Object value) {
        Map contextParams = getRequestParam();
        if (contextParams == null) {
            contextParams = new HashMap();
            setRequestParam(contextParams);
        }
        if (key != null && value != null) {
            contextParams.put(key, value);
        }
    }

    public static boolean isPrdEnv(String env) {
        return env == null;
    }

    public static boolean isUatEnv(String env) {
        return env == null;
    }
}
