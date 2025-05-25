package net.dusense.framework.core.cloud.constants;

/**
 * 应用系统常量
 *
 * @author [ saily ]
 * @email sailyfirm@gmail.com
 * @version V3.0
 * @date 2021/09/06
 */
public interface FeignConstant {

    /** 基础包 */
    String BASE_PACKAGES = "net.dusense";

    /** 应用名前缀 */
    String APP_PREFIX = "dusense-";

    /** 网关模块名称 */
    String APP_GATEWAY_NAME = APP_PREFIX + "gateway";

    /** 授权模块名称 */
    String APP_AUTH_NAME = APP_PREFIX + "authd";

    /** 监控模块名称 */
    String APP_ADMIN_NAME = APP_PREFIX + "admin";

    /** 报表系统名称 */
    String APP_REPORT_NAME = APP_PREFIX + "pagemanage";

    /** 集群监控名称 */
    String APP_TURBINE_NAME = APP_PREFIX + "turbine";

    /** websocket名称 */
    String APP_WEBSOCKET_NAME = APP_PREFIX + "pushservice";

    /** 首页模块名称 */
    String APP_DESK_NAME = APP_PREFIX + "desk";

    /** 系统模块名称 */
    String APP_SYSMAGE_NAME = APP_PREFIX + "sysmanage";

    /** 用户模块名称 */
    String APP_USER_NAME = APP_PREFIX + "user";

    /** 日志模块名称 */
    String APP_LOG_NAME = APP_PREFIX + "log";

    /** 开发模块名称 */
    String APP_DEVELOP_NAME = APP_PREFIX + "develop";

    /** 流程设计器模块名称 */
    String APP_FLOWDESIGN_NAME = APP_PREFIX + "flowdesign";

    /** 工作流模块名称 */
    String APP_FLOW_NAME = APP_PREFIX + "flow";

    /** 资源模块名称 */
    String APP_RESOURCE_NAME = APP_PREFIX + "resource";

    /** 接口文档模块名称 */
    String APP_SWAGGER_NAME = APP_PREFIX + "swagger";

    /** 测试模块名称 */
    String APP_TEST_NAME = APP_PREFIX + "test";

    /** 演示模块名称 */
    String APP_DEMO_NAME = APP_PREFIX + "demo";
}
