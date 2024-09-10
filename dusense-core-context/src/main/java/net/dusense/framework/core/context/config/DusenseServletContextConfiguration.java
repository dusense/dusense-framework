package net.dusense.framework.core.context.config;

import net.dusense.framework.core.context.DusenseRequestHeadersHandler;
import net.dusense.framework.core.context.listener.DusenseServletContextRequestListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Servlet 监听器自动配置 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class DusenseServletContextConfiguration {

    @Bean
    public ServletListenerRegistrationBean<?> registerCustomListener(
            DusenseContextProperties properties, DusenseRequestHeadersHandler httpHeadersGetter) {
        return new ServletListenerRegistrationBean<>(
                new DusenseServletContextRequestListener(properties, httpHeadersGetter));
    }
}
