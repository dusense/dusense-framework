package com.dusense.framework.extension.transaction.config;

import net.dusense.framework.core.context.props.DusensePropertySourceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/** Seata配置 @PropertySource 只能默认处理.properties 的文件配置项 */
@Configuration(proxyBeanMethods = false)
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(value = "dusense.seata.enabled", matchIfMissing = true)
@PropertySource(
        value = "classpath:/dusense-seata.yml",
        factory = DusensePropertySourceFactory.class)
public class TransactionConfiguration {}
