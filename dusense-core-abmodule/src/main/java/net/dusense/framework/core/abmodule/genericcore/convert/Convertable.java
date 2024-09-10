package net.dusense.framework.core.abmodule.genericcore.convert;

import org.mapstruct.factory.Mappers;

import java.util.Objects;

/**
 * 基于mapstruct 对象数据转换器 自动编译时生成java类 默认是不转换处理，
 *
 * @param <T1>
 * @param <T2>
 * @author [ saily ]
 * @version V3.0
 * @email sailyfirm@gmail.com
 * @date 2022/11/23
 */
public interface Convertable<T1, T2> {

    /**
     * 通过mapstruct 自身容器获取接口实现类
     *
     * @param clazz
     * @return
     */
    static Convertable instance(Class<? extends Convertable> clazz) {
        if (Objects.isNull(clazz)) {
            return null;
        }
        Convertable INSTANCE = Mappers.getMapper(clazz);
        return INSTANCE;
    }

    /**
     * => 将该对象描述成 目标对象
     *
     * @param t2
     * @return
     */
    default T1 describe(T2 t2) {
        return (T1) t2;
    }

    /**
     * <= 将目标对象转换成自身对象
     *
     * @param t1
     * @return
     */
    default T2 convert(T1 t1) {
        return (T2) t1;
    }
}
