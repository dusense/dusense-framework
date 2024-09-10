package net.dusense.framework.extension.log.config;

import net.dreamlu.mica.auto.annotation.AutoListener;
import net.dusense.framework.extension.log.LogPrintStream;
import net.dusense.framework.extension.log.ProfileEnvConstant;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * 日志启动器EnvironmentPreparedEvent 优先于LoggingApplicationListener 之前处理 EnvironmentPostProcessor
 * ConfigDataEnvironmentPostProcessor 之前完成可以
 *
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2022/05/25
 */
@AutoListener
public class LogStartingEventListener
        implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {

    @Override
    public int getOrder() {
        return LoggingApplicationListener.DEFAULT_ORDER - 1;
    }

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {

        String[] activeProfiles = event.getEnvironment().getActiveProfiles();
        List<String> activeProfileList = List.of(activeProfiles);
        String profile;
        if (activeProfileList.isEmpty()) {
            // 默认dev开发
            profile = ProfileEnvConstant.DEV_CODE;
            activeProfileList.add(profile);
        } else if (activeProfileList.size() == 1) {
            profile = activeProfileList.get(0);
        } else {
            // 同时存在dev、test、prod环境时
            throw new RuntimeException(
                    "同时存在环境变量:[" + StringUtils.arrayToCommaDelimitedString(activeProfiles) + "]");
        }
        String appName = event.getEnvironment().getProperty("spring.application.name");
        boolean isLocalDev = ProfileEnvConstant.DEV_CODE.equals(profile);
        String logConfig = System.getProperty("logging.config");
        logConfig =
                StringUtils.hasText(logConfig)
                        ? logConfig
                        : event.getEnvironment().getProperty("logging.config");
        System.out.printf(
                "----启动中，读取到的环境变量:[%s],[%s],[%s] \n",
                StringUtils.collectionToCommaDelimitedString(activeProfileList),
                appName,
                logConfig);

        // 如果jvm 参数未传递变量，加载 logconfig 配置文件
        if (!StringUtils.hasText(logConfig)) {
            // 优先处理log4j2.xml
            try {
                File log4jFile = ResourceUtils.getFile("classpath:log4j2.xml");
                if (!log4jFile.exists()) {
                    System.setProperty(
                            "logging.config",
                            String.format("classpath:log/log4j2_%s.xml", profile));
                }
            } catch (FileNotFoundException e) {
                System.out.println("----系统服务中未找到 /log4j2.xml 配置文件,将默认从dusense-extension-log 中加载");
                System.setProperty(
                        "logging.config", String.format("classpath:log/log4j2_%s.xml", profile));
            }
        }
        System.setProperty("spring.application.name", appName);
        // RocketMQ-Client 4.2.0 Log4j2 配置文件冲突问题解决：https://www.jianshu.com/p/b30ae6dd3811
        System.setProperty("rocketmq.client.log.loadconfig", "false");
        //  RocketMQ-Client 4.3 设置默认为 slf4j
        System.setProperty("rocketmq.client.logUseSlf4j", "true");
        // 非本地 将 全部的 System.err 和 System.out 替换为log
        if (!isLocalDev) {
            System.setOut(LogPrintStream.out());
            System.setErr(LogPrintStream.err());
        }
    }
}
