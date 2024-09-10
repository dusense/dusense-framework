package net.dusense.framework.core.tool.function;

import org.springframework.lang.Nullable;

/** 受检的 function */
@FunctionalInterface
public interface CheckedFunction<T, R> {

    /**
     * Run the Function
     *
     * @param t T
     * @return R R
     * @throws Throwable CheckedException
     */
    @Nullable
    R apply(@Nullable T t) throws Throwable;
}
