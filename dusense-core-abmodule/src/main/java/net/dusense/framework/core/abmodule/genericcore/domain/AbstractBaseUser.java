package net.dusense.framework.core.abmodule.genericcore.domain;

import lombok.Data;

/**
 * Description: [ 公共抽象模块 - 用户抽象实体]
 *
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2022/11/09
 */
@Data
public abstract class AbstractBaseUser {

    protected String userName;

    protected String password;

    public abstract String getUserId();

    public abstract String getDeptId();

    public abstract String getRoleId();

    public abstract String getRoleName();
}
