package net.dusense.framework.extension.tenant.hibernate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.TenantId;

/**
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2022/09/22
 */
@Data
@MappedSuperclass
public abstract class TenantEntity {

    /** 租户ID */
    @TenantId
    @Column(name = "tenant_id")
    private String tenantId;
}
