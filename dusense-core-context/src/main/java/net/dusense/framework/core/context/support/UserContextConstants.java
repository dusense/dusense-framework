package net.dusense.framework.core.context.support;

/** */
public interface UserContextConstants {
    String DEFAULT_DATE_FORMAT_PATTERN = "yyyy-MM-dd";

    String DATE_FORMAT_PATTERN_SECOND = "yyyy-MM-dd HH:mm:ss";

    /** 用来存储用户Session 的变量名 */
    String SESSION_USERINFO = "sessionUserInfo";

    String RESOURCE_TYPE_MENU = "menu";
    String RESOURCE_TYPE_BTN = "button";
    Integer EX_TOKEN_ERROR_CODE = 40101;
    // 用户token异常
    Integer EX_USER_INVALID_CODE = 40102;
    // 客户端token异常
    Integer EX_CLIENT_INVALID_CODE = 40131;
    Integer EX_CLIENT_FORBIDDEN_CODE = 40331;
    Integer EX_OTHER_CODE = 500;
    String CONTEXT_KEY_USER_ID = "currentUserId";
    String CONTEXT_KEY_USERNAME = "currentUserName";
    String CONTEXT_KEY_USER_NAME = "currentUser";
    String CONTEXT_KEY_USER_TOKEN = "currentUserToken";
    String CONTEXT_KEY_TENANT_ID = "currentTenantId";
    String CONTEXT_KEY_SESSION_ID = "currentSessionId";

    String JWT_KEY_USER_ID = "userId";
    String JWT_KEY_NAME = "name";

    String ZUUL_XFORWARD_REMOTEHOST = "ZUUL_XFORWARD_REMOTEHOST";

    String FEIGN_HEADER_REQFLAG = "feign-req";

    String HTTPCLIENT_TIMEOUT_CONNECTION = "__HTTP_CONNECTIONTIMEOUT";

    String HTTPCLIENT_TIMEOUT_SOC = "__HTTP_SOCTIMEOUT";
}
