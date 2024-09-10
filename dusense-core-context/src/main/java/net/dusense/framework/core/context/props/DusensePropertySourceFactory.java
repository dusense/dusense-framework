package net.dusense.framework.core.context.props;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.util.Properties;

/**
 * 支持@propertySource yaml & xml 格式内容解析支持，默认是properties文件
 *
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2022/04/06
 */
public class DusensePropertySourceFactory extends DefaultPropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource)
            throws IOException {

        String sourceName =
                StringUtils.isNotEmpty(name) ? name : resource.getResource().getFilename();

        if (sourceName != null && (sourceName.endsWith(".yml") || sourceName.endsWith(".yaml"))) {
            Properties propertiesFromYaml = loadYml(resource);
            // 将YML配置转成Properties之后，再用PropertiesPropertySource绑定
            return new PropertiesPropertySource(sourceName, propertiesFromYaml);
        } else {
            return super.createPropertySource(name, resource);
        }
    }

    // 将YML格式的配置转成Properties配置
    private Properties loadYml(EncodedResource resource) throws IOException {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource.getResource());
        factory.afterPropertiesSet();
        return factory.getObject();
    }
}
