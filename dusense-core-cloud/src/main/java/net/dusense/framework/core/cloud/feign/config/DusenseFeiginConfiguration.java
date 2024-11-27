package net.dusense.framework.core.cloud.feign.config;

import feign.Contract;
import feign.jaxrs.JAXRSContract;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * feign 支持 jax-rs标准
 *
 * @author [ saily ]
 * @email sailyfirm@gmail.com
 * @version V3.0
 * @date 2024/09/06
 */
@AutoConfigureBefore({FeignClientsConfiguration.class})
@Order(Integer.MAX_VALUE)
@Configuration
public class DusenseFeiginConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Contract feignContract() {
        return new JAXRSContract();
    }
}
