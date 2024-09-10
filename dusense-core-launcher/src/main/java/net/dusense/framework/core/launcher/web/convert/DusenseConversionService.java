package net.dusense.framework.core.launcher.web.convert;

import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.lang.Nullable;
import org.springframework.util.StringValueResolver;

/** 类型 转换 服务，添加了 IEnum 转换 */
public class DusenseConversionService extends ApplicationConversionService {
    @Nullable private static volatile DusenseConversionService SHARED_INSTANCE;

    public DusenseConversionService() {
        this(null);
    }

    public DusenseConversionService(@Nullable StringValueResolver embeddedValueResolver) {
        super(embeddedValueResolver);
        super.addConverter(new EnumToStringConverter());
        super.addConverter(new StringToEnumConverter());
    }

    /**
     * Return a shared default application {@code ConversionService} instance, lazily building it
     * once needed.
     *
     * <p>Note: This method actually returns an {@link DusenseConversionService} instance. However,
     * the {@code ConversionService} signature has been preserved for binary compatibility.
     *
     * @return the shared {@code DusenseConversionService} instance (never{@code null})
     */
    public static GenericConversionService getInstance() {
        DusenseConversionService sharedInstance = DusenseConversionService.SHARED_INSTANCE;
        if (sharedInstance == null) {
            synchronized (DusenseConversionService.class) {
                sharedInstance = DusenseConversionService.SHARED_INSTANCE;
                if (sharedInstance == null) {
                    sharedInstance = new DusenseConversionService();
                    DusenseConversionService.SHARED_INSTANCE = sharedInstance;
                }
            }
        }
        return sharedInstance;
    }
}
