package net.dusense.framework.core.launcher.config;

import net.dusense.framework.core.context.props.DusensePropertySourceFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * ${description}
 *
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2022/11/17
 */
@Configuration(proxyBeanMethods = false)
@Order(Ordered.HIGHEST_PRECEDENCE)
@PropertySource(
        value = "classpath:/dusense-launcher.yml",
        factory = DusensePropertySourceFactory.class)
public class DusenseLaunchConfiguration {

    //    @Bean
    //    public ResponseBodyResultHandler responseWrapper(ServerCodecConfigurer
    // serverCodecConfigurer,
    //            RequestedContentTypeResolver requestedContentTypeResolver) {
    //        ResponseBodyResultHandler handler = new
    // GlobalReactiveResponseHandler(serverCodecConfigurer.getWriters(),
    //                requestedContentTypeResolver);
    //
    //        //优先于默认之前处理
    //        handler.setOrder(-1);
    //        return handler;
    //    }
}
