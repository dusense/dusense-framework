package net.dusense.framework.core.cloud.actuator;

import org.springframework.context.annotation.Configuration;

/**
 * Spring boot actuator endpoint config
 *
 * @author [ saily ]
 * @email sailyfirm@gmail.com
 * @version V3.0
 * @date 2021/1/21
 */
@Configuration
public class WebMvcEndpointHandlerMappingConfiguration {
    //
    //    @Bean
    //    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(
    //            WebEndpointsSupplier webEndpointsSupplier,
    //            ServletEndpointsSupplier servletEndpointsSupplier,
    //            ControllerEndpointsSupplier controllerEndpointsSupplier,
    //            EndpointMediaTypes endpointMediaTypes,
    //            CorsEndpointProperties corsProperties,
    //            WebEndpointProperties webEndpointProperties,
    //            Environment environment) {
    //        List<ExposableEndpoint<?>> allEndpoints = Lists.newArrayList();
    //        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
    //        allEndpoints.addAll(webEndpoints);
    //        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
    //        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
    //        String basePath = webEndpointProperties.getBasePath();
    //        EndpointMapping endpointMapping = new EndpointMapping(basePath);
    //        boolean shouldRegisterLinksMapping =
    //                this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
    //        return new WebMvcEndpointHandlerMapping(
    //                endpointMapping,
    //                webEndpoints,
    //                endpointMediaTypes,
    //                corsProperties.toCorsConfiguration(),
    //                new EndpointLinksResolver(allEndpoints, basePath),
    //                shouldRegisterLinksMapping,
    //                null);
    //    }
    //
    //    private boolean shouldRegisterLinksMapping(
    //            WebEndpointProperties webEndpointProperties, Environment environment, String
    // basePath) {
    //        return webEndpointProperties.getDiscovery().isEnabled()
    //                && (StringUtils.hasText(basePath)
    //                        ||
    // ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
    //    }
}
