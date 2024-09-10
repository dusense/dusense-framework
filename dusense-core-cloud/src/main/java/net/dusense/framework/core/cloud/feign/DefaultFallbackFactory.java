package net.dusense.framework.core.cloud.feign;

import feign.Target;
import lombok.AllArgsConstructor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * 默认 Fallback，避免写过多fallback类
 *
 * @param <T> 泛型标记
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2022/11/21
 */
@AllArgsConstructor
public class DefaultFallbackFactory<T> implements FallbackFactory<T> {
    private final Target<T> target;

    @Override
    @SuppressWarnings("unchecked")
    public T create(Throwable cause) {
        final Class<T> targetType = target.type();
        final String targetName = target.name();
        // 使用Enhancer来给某个类创建代理类，步骤
        // 1.创建Enhancer对象
        Enhancer enhancer = new Enhancer();
        // 2.通过setSuperclass来设置父类型，即需要给哪个类创建代理类
        enhancer.setSuperclass(targetType);
        enhancer.setUseCache(true);
        /* 3.设置回调，需实现org.springframework.cglib.proxy.Callback接口，
        此处我们使用的是org.springframework.cglib.proxy.MethodInterceptor，也是一个接口，实现了Callback接口，
        当调用代理对象的任何方法的时候，都会被MethodInterceptor接口的invoke方法处理*/
        enhancer.setCallback(new DusenseFeignFallback<>(targetType, targetName, cause));
        // 4.获取代理对象,调用enhancer.create方法获取代理对象，这个方法返回的是Object类型的，所以需要强转一下
        return (T) enhancer.create();
    }
}
