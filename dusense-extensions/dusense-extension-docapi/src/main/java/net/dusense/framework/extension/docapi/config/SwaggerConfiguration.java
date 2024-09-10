package net.dusense.framework.extension.docapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import net.dusense.framework.core.context.props.DusensePropertySourceFactory;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Optional;

/**
 * swagger配置项
 *
 * @author Fan Yang
 * @description
 * @date 2017年6月20日
 * @since 1.7
 */
@Configuration
@ConfigurationProperties("dusense.docapi.swagger")
@PropertySource(
        value = "classpath:/dusense-docapi.yml",
        factory = DusensePropertySourceFactory.class)
public class SwaggerConfiguration {
    private String basePackage;

    private String developer;
    private String serviceName;
    private String description;

    @Bean
    public GroupedOpenApi userApi() {
        String[] paths = {"/**"};
        String[] packagedToMatch = {Optional.ofNullable(basePackage).orElse("net.dusense")};
        return GroupedOpenApi.builder()
                .group(Optional.ofNullable(serviceName).orElse("dusense"))
                .pathsToMatch(paths)
                .addOperationCustomizer(
                        (operation, handlerMethod) -> {
                            return operation.addParametersItem(
                                    new HeaderParameter()
                                            .name("Token")
                                            .example("测试Token")
                                            .description("Token令牌")
                                            .schema(
                                                    new StringSchema()
                                                            ._default("BR")
                                                            .name("Token")
                                                            .description("Token令牌")));
                        })
                .packagesToScan(packagedToMatch)
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title(this.serviceName + " 系统数据接口文档")
                                .description(this.description)
                                .version("4.0")
                                .termsOfService("http://net.dusense.com")
                                .license(
                                        new License()
                                                .name("Apache 2.0")
                                                .url("http://net.dusense.com")));
        //                .components(new Components().addSecuritySchemes(
        //                        HttpHeaders.AUTHORIZATION,
        //                        new SecurityScheme()
        //                                .name(HttpHeaders.AUTHORIZATION)
        //                                .type(SecurityScheme.Type.HTTP)
        //                                .scheme("bearer")));
    }
}
