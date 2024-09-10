package net.dusense.framework.extension.tenant.hibernate.support;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import net.dusense.framework.core.context.DusenseThreadLocal;
import org.hibernate.binder.internal.TenantIdBinder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;

import java.io.Serializable;
import java.util.Map;

/**
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2022/05/15
 */
@ApplicationScoped
@Slf4j
public class HibernateTenantIdentifierResolver
        implements CurrentTenantIdentifierResolver<Serializable>, HibernatePropertiesCustomizer {

    private static final DusenseThreadLocal<String> CURRENT_TENANT = new DusenseThreadLocal<>();

    public void setCurrentTenant(String currentTenant) {
        CURRENT_TENANT.set(currentTenant);
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) { // 2
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }

    @Override
    public Serializable resolveCurrentTenantIdentifier() {
        return TenantIdBinder.PARAMETER_NAME;
    }

    @Override
    public boolean isRoot(Serializable tenantId) {
        return true;
    }
}
