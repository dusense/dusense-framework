package net.dusense.framework.core.launcher.service;

import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

/** launcher 监听扩展 用于一些【启动前】-应用启动扩展 */
public interface LauncherListener extends ApplicationListener<ApplicationPreparedEvent> {

    @Override
    public default void onApplicationEvent(ApplicationPreparedEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
    }
}
