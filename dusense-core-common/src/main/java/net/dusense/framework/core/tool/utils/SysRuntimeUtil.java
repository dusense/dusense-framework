package net.dusense.framework.core.tool.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.List;

/** 运行时工具类 */
public class SysRuntimeUtil {

    /**
     * 获得当前进程的PID
     *
     * <p>当失败时返回-1
     *
     * @return pid
     */
    public static int getPid() {
        // something like '<pid>@<hostname>', at least in SUN / Oracle JVMs
        final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        final int index = jvmName.indexOf(CharPool.AT);
        if (index > 0) {
            return NumberUtils.toInt(jvmName.substring(0, index), -1);
        }
        return -1;
    }

    /**
     * 返回应用启动到现在的时间
     *
     * @return {Duration}
     */
    public static Duration getUpTime() {
        long upTime = ManagementFactory.getRuntimeMXBean().getUptime();
        return Duration.ofMillis(upTime);
    }

    /**
     * 返回输入的JVM参数列表
     *
     * @return jvm参数
     */
    public static String getJvmArguments() {
        List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        return StringUtils.join(vmArguments, StringPool.SPACE);
    }

    /**
     * 获取CPU核数
     *
     * @return cpu count
     */
    public static int getCpuNum() {
        return Runtime.getRuntime().availableProcessors();
    }
}
