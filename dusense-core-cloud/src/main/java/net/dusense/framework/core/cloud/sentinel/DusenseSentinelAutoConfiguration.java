package net.dusense.framework.core.cloud.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.BlockExceptionHandler;
import feign.Feign;
import feign.RequestInterceptor;
import lombok.AllArgsConstructor;
import net.dusense.framework.core.cloud.feign.DusenseFeignRequestInterceptor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

/** Sentinel配置类 */
@AllArgsConstructor
// @Configuration(proxyBeanMethods = false)
// @AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
// @ConditionalOnProperty(
//        name = {"spring.cloud.sentinel.enabled"},
//        matchIfMissing = true
// )
public class DusenseSentinelAutoConfiguration {

    @Bean
    @Primary
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Feign.Builder feignSentinelBuilder(RequestInterceptor requestInterceptor) {
        return DusenseFeignSentinel.builder().requestInterceptor(requestInterceptor);
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestInterceptor requestInterceptor() {
        return new DusenseFeignRequestInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public BlockExceptionHandler blockExceptionHandler() {
        return new DusenseBlockExceptionHandler();
    }
}
