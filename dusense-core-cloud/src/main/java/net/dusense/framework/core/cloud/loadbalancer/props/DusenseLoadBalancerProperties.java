package net.dusense.framework.core.cloud.loadbalancer.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

@RefreshScope
@ConfigurationProperties(DusenseLoadBalancerProperties.PROPERTIES_PREFIX)
public class DusenseLoadBalancerProperties {
    public static final String PROPERTIES_PREFIX = "dusense.loadbalancer";

    private boolean enabled = true;

    private String version;

    private List<String> priorIpPattern = new ArrayList<>();

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getVersion() {
        return this.version;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setPriorIpPattern(List<String> priorIpPattern) {
        this.priorIpPattern = priorIpPattern;
    }

    public List<String> getPriorIpPattern() {
        return this.priorIpPattern;
    }
}
