package net.dusense.framework.core.context;

import java.util.function.Supplier;

/** DusenseThreadLocal 上下文，可支持分布式线程Local */
public class DusenseThreadLocal<T> {
    private final ThreadLocal<T> CONTEXT_HOLDER;

    public DusenseThreadLocal() {
        CONTEXT_HOLDER = new ThreadLocal<T>();
    }

    public DusenseThreadLocal(ThreadLocal<T> contextHolder) {
        CONTEXT_HOLDER = contextHolder;
    }

    public void set(T context) {
        CONTEXT_HOLDER.set(context);
    }

    public T get() {
        return CONTEXT_HOLDER.get();
    }

    public void remove() {
        CONTEXT_HOLDER.remove();
    }

    public static <S> DusenseThreadLocal<S> withInitial(Supplier<? extends S> supplier) {
        return new DusenseThreadLocal<>(ThreadLocal.withInitial(supplier));
    }
}
