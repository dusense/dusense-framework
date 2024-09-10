package net.dusense.framework.extension.hibernate;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体基础信息字段
 *
 * @author [ saily ]
 * @email sailyfirm@gmail.com
 * @version V3.0
 * @date 2021/07/21
 */
@Data
@MappedSuperclass
public class BaseEntity implements Serializable {
    /** 主键id */
    private Long id;

    /** 创建人 */
    private Long createUser;

    /** 创建部门 */
    private Long createDept;

    /** 创建时间 */
    private Date createTime;

    /** 更新人 */
    private Long updateUser;

    /** 更新时间 */
    private Date updateTime;

    /** 状态[1:正常] */
    private Integer status;

    /** 状态[0:未删除,1:删除] */
    private Integer isDeleted;
}
