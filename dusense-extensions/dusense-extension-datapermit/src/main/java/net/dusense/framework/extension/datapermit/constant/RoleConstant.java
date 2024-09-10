package net.dusense.framework.extension.datapermit.constant;

/** 系统默认角色 */
public interface RoleConstant {

    String ADMINISTRATOR = "administrator";

    String HAS_ROLE_ADMINISTRATOR = "hasRole('" + ADMINISTRATOR + "')";

    String ADMIN = "admin";

    String HAS_ROLE_ADMIN = "hasAnyRole('" + ADMINISTRATOR + "', '" + ADMIN + "')";

    String USER = "user";

    String HAS_ROLE_USER = "hasRole('" + USER + "')";

    String TEST = "test";

    String HAS_ROLE_TEST = "hasRole('" + TEST + "')";
}
