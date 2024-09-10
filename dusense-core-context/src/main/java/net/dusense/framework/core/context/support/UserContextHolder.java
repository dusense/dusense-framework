package net.dusense.framework.core.context.support;

import net.dusense.framework.core.context.DusenseThreadLocal;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于用户上下文控制 基于currentThreadLocal 机制
 *
 * <p>此用户上下文信息 基于无状态控制 将被赋值在安全服务体系中:eg shiro-client subject
 */
public class UserContextHolder {
    private static final DusenseThreadLocal<Map> localThreadHolder = new DusenseThreadLocal<Map>();

    public static Map getRequestParam() {
        return localThreadHolder.get();
    }

    public static void setRequestParam(Map params) {
        localThreadHolder.set(params);
    }

    public static Object get(String key) {
        Map contextParams = getRequestParam();
        Object result = null;
        if (contextParams != null && key != null) {
            result = contextParams.get(key);
        }

        return result;
    }

    public static void set(String key, Object value) {
        Map contextParams = getRequestParam();
        if (contextParams == null) {
            contextParams = new HashMap();
            setRequestParam(contextParams);
        }
        if (key != null && value != null) {
            contextParams.put(key, value);
        }
    }

    public static String getUserId() {
        Object value = get(UserContextConstants.CONTEXT_KEY_USER_ID);
        return returnObjectValue(value);
    }

    public static String getTenantId() {
        Object value = get(UserContextConstants.CONTEXT_KEY_TENANT_ID);
        return returnObjectValue(value);
    }

    public static String getUsername() {
        Object value = get(UserContextConstants.CONTEXT_KEY_USERNAME);
        return returnObjectValue(value);
    }

    public static String getName() {
        Object value = get(UserContextConstants.CONTEXT_KEY_USER_NAME);
        return returnObjectValue(value);
    }

    public static String getToken() {
        Object value = get(UserContextConstants.CONTEXT_KEY_USER_TOKEN);
        return returnObjectValue(value);
    }

    public static String getSessionId() {
        Object value = get(UserContextConstants.CONTEXT_KEY_SESSION_ID);
        return returnObjectValue(value);
    }

    public static void setToken(String token) {
        set(UserContextConstants.CONTEXT_KEY_USER_TOKEN, token);
    }

    public static void setName(String name) {
        set(UserContextConstants.CONTEXT_KEY_USER_NAME, name);
    }

    public static void setUserId(String userId) {
        set(UserContextConstants.CONTEXT_KEY_USER_ID, userId);
    }

    public static void setTenantId(String tenantId) {
        set(UserContextConstants.CONTEXT_KEY_TENANT_ID, tenantId);
    }

    public static void setUsername(String username) {
        set(UserContextConstants.CONTEXT_KEY_USERNAME, username);
    }

    public static void setSessionId(String sessionId) {
        set(UserContextConstants.CONTEXT_KEY_SESSION_ID, sessionId);
    }

    private static String returnObjectValue(Object value) {
        return value == null ? null : value.toString();
    }

    public static void remove() {
        localThreadHolder.remove();
    }
}
