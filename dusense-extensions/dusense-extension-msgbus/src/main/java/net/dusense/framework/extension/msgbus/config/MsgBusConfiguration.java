package net.dusense.framework.extension.msgbus.config;

import lombok.extern.slf4j.Slf4j;
import net.dusense.framework.extension.msgbus.DusenseEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.function.Consumer;

@Configuration(proxyBeanMethods = false)
@PropertySource(value = "classpath:/dusense-msgbus.yml")
@Slf4j
public class MsgBusConfiguration {

    @Bean
    public Consumer<DusenseEvent> dusense() {
        return event -> {
            // Update inventory levels for the product
            log.info("DusenseEvent consumer!! ");
        };
    }
}
