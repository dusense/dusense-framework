package net.dusense.framework.extension.apicrypto.core;

import lombok.RequiredArgsConstructor;
import net.dusense.framework.core.tool.jackson.JacksonUtils;
import net.dusense.framework.extension.apicrypto.annotation.decrypt.ApiDecrypt;
import net.dusense.framework.extension.apicrypto.bean.CryptoInfoBean;
import net.dusense.framework.extension.apicrypto.config.ApiCryptoProperties;
import net.dusense.framework.extension.apicrypto.util.ApiCryptoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;

/** param 参数 解析 */
@RequiredArgsConstructor
public class ApiDecryptParamResolver implements HandlerMethodArgumentResolver {
    private final ApiCryptoProperties properties;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return AnnotatedElementUtils.hasAnnotation(parameter.getParameter(), ApiDecrypt.class);
    }

    @Nullable
    @Override
    public Object resolveArgument(
            MethodParameter methodParameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        Parameter parameter = methodParameter.getParameter();
        ApiDecrypt apiDecrypt =
                AnnotatedElementUtils.getMergedAnnotation(parameter, ApiDecrypt.class);
        String text = webRequest.getParameter(properties.getParamName());
        if (StringUtils.isBlank(text)) {
            return null;
        }
        CryptoInfoBean infoBean = new CryptoInfoBean(apiDecrypt.value(), apiDecrypt.secretKey());
        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
        byte[] decryptData = ApiCryptoUtil.decryptData(properties, textBytes, infoBean);
        return JacksonUtils.readValue(decryptData, parameter.getType());
    }
}
