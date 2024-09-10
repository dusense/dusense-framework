package net.dusense.framework.extension.shardingsphere.config;

import lombok.AllArgsConstructor;
import net.dusense.framework.core.context.props.DusensePropertySourceFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@PropertySource(
        value = "classpath:/dusense-shardingsphere.properties",
        factory = DusensePropertySourceFactory.class)
public class ShardingSphereConfiguration {}
