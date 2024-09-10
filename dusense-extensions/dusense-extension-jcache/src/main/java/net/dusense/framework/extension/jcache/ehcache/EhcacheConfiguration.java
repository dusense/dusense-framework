package net.dusense.framework.extension.jcache.ehcache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * * 基于Spring data cache 完成缓存API 处理,暂未基于jakarta标准
 *
 * @author [ saily ]
 * @email sailyfirm@gmail.com
 * @version V3.0
 * @date 2021/01/01
 */
@EnableCaching
@Configuration(proxyBeanMethods = false)
public class EhcacheConfiguration {}
