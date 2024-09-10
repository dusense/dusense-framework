package net.dusense.framework.extension.springredis.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@EnableRedisRepositories(
        basePackages = {"net.dusense.**.dao.impl"},
        enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
// @PropertySource(value = "classpath:/dusense-redis.yml")
public class RedisConfiguration {

    @Bean
    public CacheManager ehCacheManager() {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();
        return cacheManager;
    }
}
