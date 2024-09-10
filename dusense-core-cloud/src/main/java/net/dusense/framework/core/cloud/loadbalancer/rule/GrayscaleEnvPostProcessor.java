package net.dusense.framework.core.cloud.loadbalancer.rule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;

/**
 * 用于在 Spring 应用程序启动过程中对环境（Environment）进行后处理的接口。 它允许开发者在 Spring 应用程序启动之前修改 Spring 的环境属性 灰度版本控制处理。
 *
 * @author [ saily ]
 * @email sailyfirm@gmail.com
 * @version V3.0
 * @date 2021/05/25
 */
public class GrayscaleEnvPostProcessor implements EnvironmentPostProcessor, Ordered {
    private static final String GREYSCALE_KEY = "dusense.loadbalancer.version";
    private static final String METADATA_KEY = "spring.cloud.nacos.discovery.metadata.version";

    @Override
    public void postProcessEnvironment(
            ConfigurableEnvironment environment, SpringApplication application) {
        String version = environment.getProperty("dusense.loadbalancer.version");

        if (StringUtils.hasText(version)) {
            environment
                    .getSystemProperties()
                    .put("spring.cloud.nacos.discovery.metadata.version", version);
        }
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
