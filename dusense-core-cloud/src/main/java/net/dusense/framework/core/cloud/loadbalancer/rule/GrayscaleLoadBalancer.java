package net.dusense.framework.core.cloud.loadbalancer.rule;

import lombok.extern.slf4j.Slf4j;
import net.dusense.framework.core.cloud.loadbalancer.props.DusenseLoadBalancerProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PatternMatchUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * 用于实现负载均衡的接口, 根据特定的负载均衡策略，从服务注册中心（如 Nacos Eureka、Consul 等）获取服务实例列表，并根据一定的规则选择要处理请求的目标服务实
 * 灰度负载版本控制处理
 *
 * @author [ saily ]
 * @email sailyfirm@gmail.com
 * @version V3.0
 * @date 2021/05/25
 */
@Slf4j
public class GrayscaleLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    public GrayscaleLoadBalancer(
            ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
            DusenseLoadBalancerProperties loadBalancerProperties) {
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.loadBalancerProperties = loadBalancerProperties;
    }

    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    private final DusenseLoadBalancerProperties loadBalancerProperties;

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier =
                (ServiceInstanceListSupplier)
                        this.serviceInstanceListSupplierProvider.getIfAvailable(
                                org.springframework.cloud.loadbalancer.core
                                                .NoopServiceInstanceListSupplier
                                        ::new);
        return supplier.get(request)
                .next()
                .map(serviceInstances -> getInstanceResponse(serviceInstances, request));
    }

    private Response<ServiceInstance> getInstanceResponse(
            List<ServiceInstance> instances, Request request) {
        if (CollectionUtils.isEmpty(instances)) {
            return (Response<ServiceInstance>) new EmptyResponse();
        }

        List<String> priorIpPattern = this.loadBalancerProperties.getPriorIpPattern();
        if (!priorIpPattern.isEmpty()) {
            String[] priorIpPatterns = priorIpPattern.<String>toArray(new String[0]);

            List<ServiceInstance> priorIpInstances =
                    (List<ServiceInstance>)
                            instances.stream()
                                    .filter(
                                            i ->
                                                    PatternMatchUtils.simpleMatch(
                                                            priorIpPatterns, i.getHost()))
                                    .collect(Collectors.toList());
            if (!priorIpInstances.isEmpty()) {
                instances = priorIpInstances;
            }
        }

        DefaultRequestContext context = (DefaultRequestContext) request.getContext();
        RequestData requestData = (RequestData) context.getClientRequest();
        HttpHeaders headers = requestData.getHeaders();
        String versionName = headers.getFirst("version");

        if (StringUtils.isBlank(versionName)) {

            List<ServiceInstance> noneGrayscaleInstances =
                    (List<ServiceInstance>)
                            instances.stream()
                                    .filter(i -> !i.getMetadata().containsKey("version"))
                                    .collect(Collectors.toList());
            return randomInstance(noneGrayscaleInstances);
        }

        List<ServiceInstance> grayscaleInstances =
                (List<ServiceInstance>)
                        instances.stream()
                                .filter(
                                        i -> {
                                            String versionNameInMetadata =
                                                    (String) i.getMetadata().get("version");
                                            return StringUtils.equalsIgnoreCase(
                                                    versionNameInMetadata, versionName);
                                        })
                                .collect(Collectors.toList());
        return randomInstance(grayscaleInstances);
    }

    private Response<ServiceInstance> randomInstance(List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            return (Response<ServiceInstance>) new EmptyResponse();
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(instances.size());
        ServiceInstance instance = instances.get(randomIndex % instances.size());
        return (Response<ServiceInstance>) new DefaultResponse(instance);
    }
}
