package net.dusense.framework.core.tool.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/** 工具包集合，工具类快捷方式 */
@Deprecated
public class Func {

    /**
     * 断言，必须不能为 null
     *
     * <blockquote>
     *
     * <pre>
     * public Foo(Bar bar) {
     *     this.bar = $.requireNotNull(bar);
     * }
     * </pre>
     *
     * </blockquote>
     *
     * @param obj the object reference to check for nullity
     * @param <T> the type of the reference
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     */
    public static <T> T requireNotNull(T obj) {
        return Objects.requireNonNull(obj);
    }

    /**
     * 断言，必须不能为 null
     *
     * <blockquote>
     *
     * <pre>
     * public Foo(Bar bar, Baz baz) {
     *     this.bar = $.requireNotNull(bar, "bar must not be null");
     *     this.baz = $.requireNotNull(baz, "baz must not be null");
     * }
     * </pre>
     *
     * </blockquote>
     *
     * @param obj the object reference to check for nullity
     * @param message detail message to be used in the event that a {@code NullPointerException} is
     *     thrown
     * @param <T> the type of the reference
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     */
    public static <T> T requireNotNull(T obj, String message) {
        return Objects.requireNonNull(obj, message);
    }

    /**
     * 断言，必须不能为 null
     *
     * <blockquote>
     *
     * <pre>
     * public Foo(Bar bar, Baz baz) {
     *     this.bar = $.requireNotNull(bar, () -> "bar must not be null");
     * }
     * </pre>
     *
     * </blockquote>
     *
     * @param obj the object reference to check for nullity
     * @param messageSupplier supplier of the detail message to be used in the event that a {@code
     *     NullPointerException} is thrown
     * @param <T> the type of the reference
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     */
    public static <T> T requireNotNull(T obj, Supplier<String> messageSupplier) {
        return Objects.requireNonNull(obj, messageSupplier);
    }

    /**
     * 判断对象是否为null
     *
     * <p>This method exists to be used as a {@link java.util.function.Predicate}, {@code
     * filter($::isNull)}
     *
     * @param obj a reference to be checked against {@code null}
     * @return {@code true} if the provided reference is {@code null} otherwise {@code false}
     * @see java.util.function.Predicate
     */
    public static boolean isNull(@Nullable Object obj) {
        return Objects.isNull(obj);
    }

    /**
     * 判断对象是否 not null
     *
     * <p>This method exists to be used as a {@link java.util.function.Predicate}, {@code
     * filter($::notNull)}
     *
     * @param obj a reference to be checked against {@code null}
     * @return {@code true} if the provided reference is non-{@code null} otherwise {@code false}
     * @see java.util.function.Predicate
     */
    public static boolean notNull(@Nullable Object obj) {
        return Objects.nonNull(obj);
    }

    /**
     * 首字母变小写
     *
     * @param str 字符串
     * @return {String}
     */
    public static String firstCharToLower(String str) {
        return StringUtils.toRootLowerCase(str);
    }

    /**
     * 首字母变大写
     *
     * @param str 字符串
     * @return {String}
     */
    public static String firstCharToUpper(String str) {
        return StringUtils.toRootUpperCase(str);
    }

    /**
     * 判断是否为空字符串
     *
     * <pre class="code">
     * $.isBlank(null)		= true
     * $.isBlank("")		= true
     * $.isBlank(" ")		= true
     * $.isBlank("12345")	= false
     * $.isBlank(" 12345 ")	= false
     * </pre>
     *
     * @param cs the {@code CharSequence} to check (may be {@code null})
     * @return {@code true} if the {@code CharSequence} is not {@code null}, its length is greater
     *     than 0, and it does not contain whitespace only
     * @see Character#isWhitespace
     */
    public static boolean isBlank(@Nullable final CharSequence cs) {
        return StringUtils.isBlank(cs);
    }

    /**
     * 判断不为空字符串
     *
     * <pre>
     * $.isNotBlank(null)	= false
     * $.isNotBlank("")		= false
     * $.isNotBlank(" ")	= false
     * $.isNotBlank("bob")	= true
     * $.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is not empty and not null and not whitespace
     * @see Character#isWhitespace
     */
    public static boolean isNotBlank(@Nullable final CharSequence cs) {
        return StringUtils.isNotBlank(cs);
    }

    /**
     * 判断是否有任意一个 空字符串
     *
     * @param css CharSequence
     * @return boolean
     */
    public static boolean isAnyBlank(final CharSequence... css) {
        return StringUtils.isAnyBlank(css);
    }

    /**
     * 判断是否全为非空字符串
     *
     * @param css CharSequence
     * @return boolean
     */
    public static boolean isNoneBlank(final CharSequence... css) {
        return StringUtils.isNoneBlank(css);
    }

    /**
     * 判断对象是数组
     *
     * @param obj the object to check
     * @return 是否数组
     */
    public static boolean isArray(@Nullable Object obj) {
        return ObjectUtils.isArray(obj);
    }

    /**
     * 判断空对象 object、map、list、set、字符串、数组
     *
     * @param obj the object to check
     * @return 数组是否为空
     */
    public static boolean isEmpty(@Nullable Object obj) {
        return ObjectUtils.isEmpty(obj);
    }

    /**
     * 对象不为空 object、map、list、set、字符串、数组
     *
     * @param obj the object to check
     * @return 是否不为空
     */
    public static boolean isNotEmpty(@Nullable Object obj) {
        return !ObjectUtils.isEmpty(obj);
    }

    /**
     * 判断数组为空
     *
     * @param array the array to check
     * @return 数组是否为空
     */
    public static boolean isEmpty(@Nullable Object[] array) {
        return ObjectUtils.isEmpty(array);
    }

    /**
     * 判断数组不为空
     *
     * @param array 数组
     * @return 数组是否不为空
     */
    public static boolean isNotEmpty(@Nullable Object[] array) {
        return ObjectUtils.isNotEmpty(array);
    }

    /**
     * 对象组中是否存在 Empty Object
     *
     * @param os 对象组
     * @return boolean
     */
    public static boolean hasEmpty(Object... os) {
        for (Object o : os) {
            if (isEmpty(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对象组中是否全部为 Empty Object
     *
     * @param os 对象组
     * @return boolean
     */
    public static boolean isAllEmpty(Object... os) {
        for (Object o : os) {
            if (isNotEmpty(o)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 比较两个对象是否相等。<br>
     * 相同的条件有两个，满足其一即可：<br>
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 是否相等
     */
    public static boolean equals(Object obj1, Object obj2) {
        return Objects.equals(obj1, obj2);
    }

    /**
     * 强转string,并去掉多余空格
     *
     * @param str 字符串
     * @return {String}
     */
    public static String toStr(Object str) {
        return toStr(str, "");
    }

    /**
     * 强转string,并去掉多余空格
     *
     * @param str 字符串
     * @param defaultValue 默认值
     * @return {String}
     */
    public static String toStr(Object str, String defaultValue) {
        if (null == str || str.equals(StringPool.NULL)) {
            return defaultValue;
        }
        return String.valueOf(str);
    }

    /**
     * 强转string(包含空字符串),并去掉多余空格
     *
     * @param str 字符串
     * @param defaultValue 默认值
     * @return {String}
     */
    public static String toStrWithEmpty(Object str, String defaultValue) {
        if (null == str || str.equals(StringPool.NULL) || str.equals(StringPool.EMPTY)) {
            return defaultValue;
        }
        return String.valueOf(str);
    }

    /**
     * 判断一个字符串是否是数字
     *
     * @param cs the CharSequence to check, may be null
     * @return {boolean}
     */
    public static boolean isNumeric(final CharSequence cs) {
        return StringUtils.isNumeric(cs);
    }

    /**
     * Convert a <code>String</code> to an <code>Boolean</code>, returning a default value if the
     * conversion fails.
     *
     * <p>If the string is <code>null</code>, the default value is returned.
     *
     * <pre>
     *   $.toBoolean("true", true)  = true
     *   $.toBoolean("false")   	= false
     *   $.toBoolean("", false)  	= false
     * </pre>
     *
     * @param value the string to convert, may be null
     * @return the int represented by the string, or the default if conversion fails
     */
    public static Boolean toBoolean(Object value) {
        return toBoolean(value, null);
    }

    /**
     * Convert a <code>String</code> to an <code>Boolean</code>, returning a default value if the
     * conversion fails.
     *
     * <p>If the string is <code>null</code>, the default value is returned.
     *
     * <pre>
     *   $.toBoolean("true", true)  = true
     *   $.toBoolean("false")   	= false
     *   $.toBoolean("", false)  	= false
     * </pre>
     *
     * @param value the string to convert, may be null
     * @param defaultValue the default value
     * @return the int represented by the string, or the default if conversion fails
     */
    public static Boolean toBoolean(Object value, Boolean defaultValue) {
        if (value != null) {
            String val = String.valueOf(value);
            val = val.toLowerCase().trim();
            return Boolean.parseBoolean(val);
        }
        return defaultValue;
    }

    /**
     * 转换为Integer数组<br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static Integer[] toIntArray(String str) {
        return toIntArray(",", str);
    }

    /**
     * 转换为Integer数组<br>
     *
     * @param split 分隔符
     * @param str 被转换的值
     * @return 结果
     */
    public static Integer[] toIntArray(String split, String str) {
        if (StringUtils.isEmpty(str)) {
            return new Integer[] {};
        }
        String[] arr = str.split(split);
        final Integer[] ints = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            //            final Integer v = toInt(arr[i], 0);
            //            ints[i] = v;
        }
        return ints;
    }

    /**
     * 转换为Integer集合<br>
     *
     * @param str 结果被转换的值
     * @return 结果
     */
    public static List<Integer> toIntList(String str) {
        return Arrays.asList(toIntArray(str));
    }

    /**
     * 转换为Integer集合<br>
     *
     * @param split 分隔符
     * @param str 被转换的值
     * @return 结果
     */
    public static List<Integer> toIntList(String split, String str) {
        return Arrays.asList(toIntArray(split, str));
    }

    /**
     * 获取第一位Integer数值
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static Integer firstInt(String str) {
        return firstInt(",", str);
    }

    /**
     * 获取第一位Integer数值
     *
     * @param split 分隔符
     * @param str 被转换的值
     * @return 结果
     */
    public static Integer firstInt(String split, String str) {
        List<Integer> ints = toIntList(split, str);
        if (isEmpty(ints)) {
            return null;
        } else {
            return ints.get(0);
        }
    }

    /**
     * 转换为Long数组<br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static Long[] toLongArray(String str) {
        return toLongArray(",", str);
    }

    /**
     * 转换为Long数组<br>
     *
     * @param split 分隔符
     * @param str 被转换的值
     * @return 结果
     */
    public static Long[] toLongArray(String split, String str) {
        if (StringUtils.isEmpty(str)) {
            return new Long[] {};
        }
        String[] arr = str.split(split);
        final Long[] longs = new Long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            //            final Long v = toLong(arr[i], 0);
            //            longs[i] = v;
        }
        return longs;
    }

    /**
     * 转换为Long集合<br>
     *
     * @param str 结果被转换的值
     * @return 结果
     */
    public static List<Long> toLongList(String str) {
        return Arrays.asList(toLongArray(str));
    }

    /**
     * 转换为Long集合<br>
     *
     * @param split 分隔符
     * @param str 被转换的值
     * @return 结果
     */
    public static List<Long> toLongList(String split, String str) {
        return Arrays.asList(toLongArray(split, str));
    }

    /**
     * 获取第一位Long数值
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static Long firstLong(String str) {
        return firstLong(",", str);
    }

    /**
     * 获取第一位Long数值
     *
     * @param split 分隔符
     * @param str 被转换的值
     * @return 结果
     */
    public static Long firstLong(String split, String str) {
        List<Long> longs = toLongList(split, str);
        if (isEmpty(longs)) {
            return null;
        } else {
            return longs.get(0);
        }
    }

    /**
     * 转换为String数组<br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static String[] toStrArray(String str) {
        return toStrArray(",", str);
    }

    /**
     * 转换为String数组<br>
     *
     * @param split 分隔符
     * @param str 被转换的值
     * @return 结果
     */
    public static String[] toStrArray(String split, String str) {
        if (isBlank(str)) {
            return new String[] {};
        }
        return str.split(split);
    }

    /**
     * 转换为String集合<br>
     *
     * @param str 结果被转换的值
     * @return 结果
     */
    public static List<String> toStrList(String str) {
        return Arrays.asList(toStrArray(str));
    }

    /**
     * 转换为String集合<br>
     *
     * @param split 分隔符
     * @param str 被转换的值
     * @return 结果
     */
    public static List<String> toStrList(String split, String str) {
        return Arrays.asList(toStrArray(split, str));
    }

    /**
     * 获取第一位String数值
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static String firstStr(String str) {
        return firstStr(",", str);
    }

    /**
     * 获取第一位String数值
     *
     * @param split 分隔符
     * @param str 被转换的值
     * @return 结果
     */
    public static String firstStr(String split, String str) {
        List<String> strs = toStrList(split, str);
        if (isEmpty(strs)) {
            return null;
        } else {
            return strs.get(0);
        }
    }
}
