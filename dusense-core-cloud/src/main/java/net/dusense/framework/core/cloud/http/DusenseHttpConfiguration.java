package net.dusense.framework.core.cloud.http;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/** http 配置 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(DusenseHttpProperties.class)
public class DusenseHttpConfiguration {}
