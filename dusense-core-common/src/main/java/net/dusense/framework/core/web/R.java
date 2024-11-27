package net.dusense.framework.core.web;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.dusense.framework.core.tool.constant.AppConstant;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/** 统一API响应结果封装 */
@Accessors(chain = true)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean success;
    private String msg;

    protected T result;
    protected int status;
    private long total;
    private long timestamp;
    /** 过滤字段：指定需要序列化的字段 */
    private transient Map<Class<?>, Set<String>> includes;

    /** 过滤字段：指定不需要序列化的字段 */
    private transient Map<Class<?>, Set<String>> excludes;

    private R(IRespCode resultCode) {
        this(resultCode, null, resultCode.getMessage());
    }

    private R(IRespCode resultCode, String msg) {
        this(resultCode, null, msg);
    }

    private R(IRespCode resultCode, T data) {
        this(resultCode, data, resultCode.getMessage());
    }

    private R(IRespCode resultCode, T data, String msg) {
        this(resultCode.getCode(), data, msg);
    }

    private R(int status, T data, String msg) {
        this.status = status;
        this.result = data;
        this.msg = msg;
        this.success = RespCode.SUCCESS.code == this.status;
        this.putTimeStamp();
    }

    /**
     * 判断返回是否为成功
     *
     * @param result Result
     * @return 是否成功
     */
    public static boolean isSuccess(@Nullable R<?> result) {
        return Optional.ofNullable(result)
                .map(x -> RespCode.SUCCESS.code == x.status)
                .orElse(Boolean.FALSE);
    }

    /**
     * 判断返回是否为成功
     *
     * @param result Result
     * @return 是否成功
     */
    public static boolean isNotSuccess(@Nullable R<?> result) {
        return !R.isSuccess(result);
    }

    public static <T> R<T> ok() {
        return new R<T>(RespCode.SUCCESS);
    }

    public static <T> R<T> ok(T result) {
        return new R<T>(RespCode.SUCCESS, result);
    }

    /**
     * 返回R
     *
     * @param data 数据
     * @param <T> T 泛型标记
     * @return R
     */
    public static <T> R<T> data(T data) {
        return data(data, AppConstant.DEFAULT_SUCCESS_MESSAGE);
    }

    /**
     * 返回R
     *
     * @param data 数据
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return R
     */
    public static <T> R<T> data(T data, String msg) {
        return data(HttpServletResponse.SC_OK, data, msg);
    }

    /**
     * 返回R
     *
     * @param code 状态码
     * @param data 数据
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return R
     */
    public static <T> R<T> data(int code, T data, String msg) {
        return new R<>(code, data, data == null ? AppConstant.DEFAULT_NULL_MESSAGE : msg);
    }

    /**
     * 返回R
     *
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return R
     */
    public static <T> R<T> success(String msg) {
        return new R<>(RespCode.SUCCESS, msg);
    }

    /**
     * 返回R
     *
     * @param resultCode 业务代码
     * @param <T> T 泛型标记
     * @return R
     */
    public static <T> R<T> success(IRespCode resultCode) {
        return new R<>(resultCode);
    }

    /**
     * 返回R
     *
     * @param resultCode 业务代码
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return R
     */
    public static <T> R<T> success(IRespCode resultCode, String msg) {
        return new R<>(resultCode, msg);
    }

    /**
     * 返回R
     *
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return R
     */
    public static <T> R<T> fail(String msg) {
        return new R<>(RespCode.FAILURE, msg);
    }

    /**
     * 返回R
     *
     * @param code 状态码
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return R
     */
    public static <T> R<T> fail(int code, String msg) {
        return new R<>(code, null, msg);
    }

    /**
     * 返回R
     *
     * @param resultCode 业务代码
     * @param <T> T 泛型标记
     * @return R
     */
    public static <T> R<T> fail(IRespCode resultCode) {
        return new R<>(resultCode);
    }

    /**
     * 返回R
     *
     * @param resultCode 业务代码
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return R
     */
    public static <T> R<T> fail(IRespCode resultCode, String msg) {
        return new R<>(resultCode, msg);
    }

    /**
     * 返回R
     *
     * @param flag 成功状态
     * @return R
     */
    public static <T> R<T> status(boolean flag) {
        return flag
                ? success(AppConstant.DEFAULT_SUCCESS_MESSAGE)
                : fail(AppConstant.DEFAULT_FAILURE_MESSAGE);
    }

    public R<T> putTimeStamp() {
        this.timestamp = System.currentTimeMillis();
        return this;
    }

    public R<T> pageTotal(Long total) {
        this.total = total;
        return this;
    }

    public R<T> include(Class<?> type, String... fields) {
        return include(type, Arrays.asList(fields));
    }

    public R<T> include(Class<?> type, Collection<String> fields) {
        if (includes == null) {
            includes = new HashMap<>();
        }
        if (fields == null || fields.isEmpty()) {
            return this;
        }
        for (String field : fields) {
            if (field.contains(".")) {
                String tmp[] = field.split("[.]", 2);
                try {
                    Field field1 = type.getDeclaredField(tmp[0]);
                    if (field1 != null) {
                        include(field1.getType(), tmp[1]);
                    }
                } catch (Throwable e) {
                }
            } else {
                getStringListFromMap(includes, type).add(field);
            }
        }
        ;
        return this;
    }

    public R<T> exclude(Class type, Collection<String> fields) {
        if (excludes == null) {
            excludes = new HashMap<>();
        }
        if (fields == null || fields.isEmpty()) {
            return this;
        }
        for (String field : fields) {
            if (field.contains(".")) {
                String tmp[] = field.split("[.]", 2);
                try {
                    Field field1 = type.getDeclaredField(tmp[0]);
                    if (field1 != null) {
                        exclude(field1.getType(), tmp[1]);
                    }
                } catch (Throwable e) {
                }
            } else {
                getStringListFromMap(excludes, type).add(field);
            }
        }
        ;
        return this;
    }

    public R<T> exclude(Collection<String> fields) {
        if (excludes == null) {
            excludes = new HashMap<>();
        }
        if (fields == null || fields.isEmpty()) {
            return this;
        }
        Class type;
        if (getResult() != null) {
            type = getResult().getClass();
        } else {
            return this;
        }
        exclude(type, fields);
        return this;
    }

    public R<T> include(Collection<String> fields) {
        if (includes == null) {
            includes = new HashMap<>();
        }
        if (fields == null || fields.isEmpty()) {
            return this;
        }
        Class type;
        if (getResult() != null) {
            type = getResult().getClass();
        } else {
            return this;
        }
        include(type, fields);
        return this;
    }

    public R<T> exclude(Class type, String... fields) {
        return exclude(type, Arrays.asList(fields));
    }

    public R<T> exclude(String... fields) {
        return exclude(Arrays.asList(fields));
    }

    public R<T> include(String... fields) {
        return include(Arrays.asList(fields));
    }

    protected Set<String> getStringListFromMap(Map<Class<?>, Set<String>> map, Class type) {
        // return map.computeIfAbsent(type, new HashSet<>());
        return null;
    }
}
