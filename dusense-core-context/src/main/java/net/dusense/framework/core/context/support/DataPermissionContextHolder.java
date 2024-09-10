package net.dusense.framework.core.context.support;

import net.dusense.framework.core.context.DusenseThreadLocal;

import java.util.Map;

/** 用于数据权限的控制 基于currentThreadLocal 机制 */
public class DataPermissionContextHolder {

    public static DusenseThreadLocal<Map<String, Object>> globalScope =
            new DusenseThreadLocal<Map<String, Object>>();

    public static DusenseThreadLocal<Map<String, Object>> roleScope =
            new DusenseThreadLocal<Map<String, Object>>();
}
