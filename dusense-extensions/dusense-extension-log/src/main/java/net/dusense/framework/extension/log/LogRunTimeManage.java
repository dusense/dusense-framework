package net.dusense.framework.extension.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggerConfiguration;
import org.springframework.boot.logging.LoggingSystem;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 日志运行时管理
 *
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2022/02/27
 */
// @AutoListener
@Slf4j
public class LogRunTimeManage {

    @Autowired private LoggingSystem loggingSystem;

    public void setRootLoggerLevel(String level) {

        LoggerConfiguration loggerConfiguration =
                loggingSystem.getLoggerConfiguration(LoggingSystem.ROOT_LOGGER_NAME);

        if (loggerConfiguration == null) {
            if (log.isErrorEnabled()) {
                log.error("no loggerConfiguration with loggerName " + level);
            }
            return;
        }

        if (!supportLevels().contains(level)) {
            if (log.isErrorEnabled()) {
                log.error("current Level is not support : " + level);
            }
            return;
        }

        if (!loggerConfiguration.getEffectiveLevel().equals(LogLevel.valueOf(level))) {
            if (log.isInfoEnabled()) {
                log.info(
                        "setRootLoggerLevel success,old level is  '"
                                + loggerConfiguration.getEffectiveLevel()
                                + "' , new level is '"
                                + level
                                + "'");
            }
            loggingSystem.setLogLevel(LoggingSystem.ROOT_LOGGER_NAME, LogLevel.valueOf(level));
        }
    }

    private List<String> supportLevels() {
        return loggingSystem.getSupportedLogLevels().stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    /** 执行LoggingSystem初始化的前置操作 */
    private void onApplicationStartingEvent(ApplicationStartingEvent event) {
        // 获取LoggingSystem的真实实现，
        // 此处会根据不同的日志框架获取不同的实现，
        // logback ： LogbackLoggingSystem
        // log4j2： Log4J2LoggingSystem
        // javalog： JavaLoggingSystem
        this.loggingSystem = LoggingSystem.get(event.getSpringApplication().getClassLoader());
        // 执行beforeInitialize方法完成初始化前置操作
        this.loggingSystem.beforeInitialize();
    }

    public void setLoggerLevel(List<LoggerConfig> configList) {

        Optional.ofNullable(configList)
                .orElse(Collections.emptyList())
                .forEach(
                        config -> {
                            LoggerConfiguration loggerConfiguration =
                                    loggingSystem.getLoggerConfiguration(config.getLoggerName());

                            if (loggerConfiguration == null) {
                                if (log.isErrorEnabled()) {
                                    log.error(
                                            "no loggerConfiguration with loggerName "
                                                    + config.getLoggerName());
                                }
                                return;
                            }

                            if (!supportLevels().contains(config.getLevel())) {
                                if (log.isErrorEnabled()) {
                                    log.error(
                                            "current Level is not support : " + config.getLevel());
                                }
                                return;
                            }

                            if (log.isInfoEnabled()) {
                                log.info(
                                        "setLoggerLevel success for logger '"
                                                + config.getLoggerName()
                                                + "' ,old level is  '"
                                                + loggerConfiguration.getEffectiveLevel()
                                                + "' , new level is '"
                                                + config.getLevel()
                                                + "'");
                            }
                            loggingSystem.setLogLevel(
                                    config.getLoggerName(), LogLevel.valueOf(config.getLevel()));
                        });
    }
}
