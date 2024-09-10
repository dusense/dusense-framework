package net.dusense.framework.core.cloud.client;

import net.dusense.framework.core.cloud.constants.FeignConstant;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.*;

/**
 * ${description}
 *
 * @author [ saily ]
 * @email sailyfirm@gmail.com
 * @version V3.0
 * @date 2021/03/16
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableDiscoveryClient
@EnableFeignClients(FeignConstant.BASE_PACKAGES)
@SpringBootApplication
public @interface DusenseCloudApplication {}
