package net.dusense.framework.extension.msgbus.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@Import({PropertyPlaceholderAutoConfiguration.class})
@ConfigurationProperties(prefix = "spring.cloud.stream.redis.binder")
public class RedisMessageChannelBinderConfiguration {

    private String[] headers;

    //	@Autowired
    //	private Codec codec;

    @Autowired private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisMessageChannelBinder redisMessageChannelBinder() {

        RedisMessageChannelBinder redisMessageChannelBinder =
                new RedisMessageChannelBinder(this.redisConnectionFactory, this.headers);
        // redisMessageChannelBinder.setCodec(this.codec);
        return redisMessageChannelBinder;
    }

    public String[] getHeaders() {
        return this.headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }
}
