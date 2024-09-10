package net.dusense.framework.extension.tenant.hibernate;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import net.dusense.framework.extension.hibernate.BaseEntity;

/**
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2022/09/22
 */
@Data
@MappedSuperclass
public abstract class TenantBaseEntity extends BaseEntity {}
