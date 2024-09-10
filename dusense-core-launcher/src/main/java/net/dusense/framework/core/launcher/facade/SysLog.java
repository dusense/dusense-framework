package net.dusense.framework.core.launcher.facade;

import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;

/** 系統日志服務 */
@Data
public class SysLog implements Serializable {

    @Id private String id;

    private String menu;

    private String opt;

    private String uri;

    private String method;

    private Long createTime;

    private String userId;

    private String tenantId;

    private String userName;

    private String ip;
}
