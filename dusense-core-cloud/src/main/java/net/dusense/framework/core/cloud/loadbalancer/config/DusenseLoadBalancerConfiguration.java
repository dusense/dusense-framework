package net.dusense.framework.core.cloud.loadbalancer.config;

import net.dusense.framework.core.cloud.loadbalancer.props.DusenseLoadBalancerProperties;
import net.dusense.framework.core.cloud.loadbalancer.rule.GrayscaleLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientSpecification;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

@AutoConfigureBefore({LoadBalancerClientConfiguration.class})
@EnableConfigurationProperties({DusenseLoadBalancerProperties.class})
@ConditionalOnProperty(
        value = {"dusense.loadbalancer.enabled"},
        matchIfMissing = true)
@Order(Integer.MAX_VALUE)
public class DusenseLoadBalancerConfiguration {
    public static final int REACTIVE_SERVICE_INSTANCE_SUPPLIER_ORDER = 193827465;

    @Bean
    public ReactorLoadBalancer<ServiceInstance> reactorServiceInstanceLoadBalancer(
            Environment environment,
            LoadBalancerClientFactory loadBalancerClientFactory,
            DusenseLoadBalancerProperties balancerProperties) {
        String name = environment.getProperty("loadbalancer.client.name");
        return (ReactorLoadBalancer<ServiceInstance>)
                new GrayscaleLoadBalancer(
                        loadBalancerClientFactory.getLazyProvider(
                                name, ServiceInstanceListSupplier.class),
                        balancerProperties);
    }

    @Bean
    public LoadBalancerClientSpecification loadBalancerClientSpecification() {
        return new LoadBalancerClientSpecification(
                "default.dusenseLoadBalancerConfiguration",
                new Class[] {DusenseLoadBalancerConfiguration.class});
    }
}
