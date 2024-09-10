package net.dusense.framework.extension.log.config;

import net.dreamlu.mica.auto.annotation.AutoEnvPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 日志启动器EnvironmentPreparedEvent 优先于LoggingApplicationListener 之前处理 EnvironmentPostProcessor
 * ConfigDataEnvironmentPostProcessor 之前完成可以
 *
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2022/05/25
 */
@AutoEnvPostProcessor
public class LogEnvPostProcessor implements EnvironmentPostProcessor, Ordered {

    @Override
    public int getOrder() {
        return ConfigDataEnvironmentPostProcessor.ORDER - 1;
    }

    @Override
    public void postProcessEnvironment(
            ConfigurableEnvironment environment, SpringApplication application) {
        // 不允许naocs 的日志
        System.setProperty("nacos.logging.default.config.enabled", "false");
    }
}
