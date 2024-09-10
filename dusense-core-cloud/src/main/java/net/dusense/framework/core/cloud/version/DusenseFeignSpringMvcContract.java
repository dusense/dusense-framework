package net.dusense.framework.core.cloud.version;

import feign.MethodMetadata;
import net.dusense.framework.core.cloud.annotation.ApiVersion;
import net.dusense.framework.core.cloud.annotation.UrlVersion;
import net.dusense.framework.core.tool.utils.StringPool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.openfeign.AnnotatedParameterProcessor;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @see UrlVersion
 * @see ApiVersion
 */
public class DusenseFeignSpringMvcContract extends SpringMvcContract {

    public DusenseFeignSpringMvcContract(
            List<AnnotatedParameterProcessor> annotatedParameterProcessors,
            ConversionService conversionService) {
        super(annotatedParameterProcessors, conversionService);
    }

    @Override
    protected void processAnnotationOnMethod(
            MethodMetadata data, Annotation methodAnnotation, Method method) {
        if (RequestMapping.class.isInstance(methodAnnotation)
                || methodAnnotation.annotationType().isAnnotationPresent(RequestMapping.class)) {
            Class<?> targetType = method.getDeclaringClass();
            // url 上的版本，优先获取方法上的版本
            UrlVersion urlVersion =
                    AnnotatedElementUtils.findMergedAnnotation(method, UrlVersion.class);
            // 再次尝试类上的版本
            if (urlVersion == null || StringUtils.isBlank(urlVersion.value())) {
                urlVersion =
                        AnnotatedElementUtils.findMergedAnnotation(targetType, UrlVersion.class);
            }
            if (urlVersion != null && StringUtils.isNotBlank(urlVersion.value())) {
                String versionUrl = "/" + urlVersion.value();
                data.template().uri(versionUrl);
            }

            // 注意：在父类之前 添加 url版本，在父类之后，处理 Media Types 版本
            super.processAnnotationOnMethod(data, methodAnnotation, method);

            // 处理 Media Types 版本信息
            ApiVersion apiVersion =
                    AnnotatedElementUtils.findMergedAnnotation(method, ApiVersion.class);
            // 再次尝试类上的版本
            if (apiVersion == null || StringUtils.isBlank(apiVersion.value())) {
                apiVersion =
                        AnnotatedElementUtils.findMergedAnnotation(targetType, ApiVersion.class);
            }
            if (apiVersion != null && StringUtils.isNotBlank(apiVersion.value())) {
                DusenseMediaType mediaType = new DusenseMediaType(apiVersion.value());
                data.template().header(HttpHeaders.ACCEPT, mediaType.toString());
            }
        }
    }

    /**
     * 参考：https://gist.github.com/rmfish/0ed59a9af6c05157be2a60c9acea2a10
     *
     * @param annotations 注解
     * @param paramIndex 参数索引
     * @return 是否 http 注解
     */
    @Override
    protected boolean processAnnotationsOnParameter(
            MethodMetadata data, Annotation[] annotations, int paramIndex) {
        boolean httpAnnotation = super.processAnnotationsOnParameter(data, annotations, paramIndex);
        // 在 springMvc 中如果是 Get 请求且参数中是对象 没有声明为@RequestBody 则默认为 Param
        if (!httpAnnotation && StringPool.GET.equals(data.template().method().toUpperCase())) {
            for (Annotation parameterAnnotation : annotations) {
                if (!(parameterAnnotation instanceof RequestBody)) {
                    return false;
                }
            }
            data.queryMapIndex(paramIndex);
            return true;
        }
        return httpAnnotation;
    }
}
