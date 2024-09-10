package net.dusense.framework.extension.log;

import lombok.Data;

/**
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2022/02/27
 */
@Data
public class LoggerConfig {

    /** the name of the logger */
    private String loggerName;

    /**
     * the log level
     *
     * @see LogLevel
     */
    private String level;
}
