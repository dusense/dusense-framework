package net.dusense.framework.core.web;

import java.io.Serializable;

/** 业务代码接口 */
public interface IRespCode extends Serializable {

    /**
     * 获取消息
     *
     * @return
     */
    String getMessage();

    /**
     * 获取状态码
     *
     * @return
     */
    int getCode();
}
