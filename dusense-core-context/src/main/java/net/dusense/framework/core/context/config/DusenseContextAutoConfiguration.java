package net.dusense.framework.core.context.config;

import net.dusense.framework.core.context.DusenseRequestContext;
import net.dusense.framework.core.context.DusenseRequestHeadersHandler;
import net.dusense.framework.core.context.DusenseServletContext;
import net.dusense.framework.core.context.ServletHttpHeadersHandler;
import net.dusense.framework.core.context.props.DusenseProperties;
import net.dusense.framework.core.context.support.SpringContextHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 服务上下文配置
 *
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2022/03/16
 */
@Configuration(proxyBeanMethods = false)
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties({DusenseContextProperties.class, DusenseProperties.class})
public class DusenseContextAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SpringContextHolder buildSpringContextHolder() {
        return new SpringContextHolder();
    }

    @Bean
    @ConditionalOnMissingBean
    public DusenseRequestHeadersHandler buildHttpHeadersGetter(
            DusenseContextProperties contextProperties) {
        return new ServletHttpHeadersHandler(contextProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public DusenseRequestContext buildDusenseRequestContext(
            DusenseContextProperties contextProperties,
            DusenseRequestHeadersHandler httpHeadersGetter) {
        return new DusenseServletContext(contextProperties, httpHeadersGetter);
    }
}
