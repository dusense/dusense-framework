/*
 *      Copyright (c) 2018-2028, DreamLu All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: DreamLu 卢春梦 (596392912@qq.com)
 */

package net.dusense.framework.extension.springredis.config;

import net.dusense.framework.extension.springredis.cache.DusenseRedisClient;
import net.dusense.framework.extension.springredis.serializer.RedisKeySerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * RedisTemplate 配置
 *
 * @author L.cm
 */
@EnableCaching
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore({RedisAutoConfiguration.class})
@EnableConfigurationProperties(DusenseRedisProperties.class)
public class RedisTemplateConfiguration implements DusenseRedisSerializerConfigable {

    /**
     * value 值 序列化
     *
     * @return RedisSerializer
     */
    @Bean
    @ConditionalOnMissingBean(RedisSerializer.class)
    @Override
    public RedisSerializer<Object> redisSerializer(DusenseRedisProperties properties) {
        return defaultRedisSerializer(properties);
    }

    @Bean(name = "redisTemplate")
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory,
            RedisSerializer<Object> redisSerializer) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // key 序列化
        RedisKeySerializer keySerializer = new RedisKeySerializer();
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        // value 序列化
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(ValueOperations.class)
    public ValueOperations valueOperations(
            @Qualifier("redisTemplate") RedisTemplate redisTemplate) {
        return redisTemplate.opsForValue();
    }

    @Bean
    public DusenseRedisClient dusenseRedisClient(RedisTemplate<String, Object> redisTemplate) {
        return new DusenseRedisClient(redisTemplate);
    }
}
