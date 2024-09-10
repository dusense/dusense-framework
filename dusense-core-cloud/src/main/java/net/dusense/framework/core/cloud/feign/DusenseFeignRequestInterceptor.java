package net.dusense.framework.core.cloud.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import net.dusense.framework.core.context.support.UserContextConstants;
import net.dusense.framework.core.context.util.ThreadLocalUtil;
import net.dusense.framework.core.tool.constant.AppConstant;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * feign 传递Request header
 *
 * <p>https://blog.csdn.net/u014519194/article/details/77160958
 * http://tietang.wang/2016/02/25/hystrix/Hystrix%E5%8F%82%E6%95%B0%E8%AF%A6%E8%A7%A3/
 * https://github.com/Netflix/Hystrix/issues/92#issuecomment-260548068
 */
@Configuration
public class DusenseFeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpHeaders headers = ThreadLocalUtil.get(AppConstant.CONTEXT_KEY);
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(
                    (key, values) -> values.forEach(value -> requestTemplate.header(key, value)));
        }

        try {
            /** 可用来做服务调度时安全认证问题 添加认证头信息 */
            // 用来区分调用是否为fegin client 微服务调度 还是 正常http

            // 让Feign service 可依据此head 返回内容区分
            requestTemplate.header(UserContextConstants.FEIGN_HEADER_REQFLAG, "1");

            // @TODO 采用Oauth2 code模式认证体系
            //            requestTemplate.queries();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
