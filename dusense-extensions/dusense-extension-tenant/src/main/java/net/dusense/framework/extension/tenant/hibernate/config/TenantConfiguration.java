package net.dusense.framework.extension.tenant.hibernate.config;

import net.dusense.framework.extension.tenant.hibernate.support.HibernateTenantIdentifierResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class TenantConfiguration {

    @Bean
    public HibernateTenantIdentifierResolver buildHibernateTenantIdentifierResolver() {
        return new HibernateTenantIdentifierResolver();
    }
}
