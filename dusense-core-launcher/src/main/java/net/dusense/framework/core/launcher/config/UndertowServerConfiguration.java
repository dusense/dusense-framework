package net.dusense.framework.core.launcher.config;

import io.undertow.Undertow;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.undertow.UndertowOptions.ENABLE_HTTP2;

/**
 *
 * <p>Undertow http2 h2c 配置，对 servlet 开启</p>
 *
 * @author [ saily ]
 * @email sailyfirm@gmail.com
 * @version V3.0
 * @date 2021/09/06
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Undertow.class)
@AutoConfigureBefore(ServletWebServerFactoryAutoConfiguration.class)
public class UndertowServerConfiguration {

    @Bean
    public WebServerFactoryCustomizer<UndertowServletWebServerFactory>
            undertowHttp2WebServerFactoryCustomizer() {
        return factory ->
                factory.addBuilderCustomizers(
                        builder -> builder.setServerOption(ENABLE_HTTP2, true));
    }
}
