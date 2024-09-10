// package net.dusense.framework.core.launcher.web.jsrrs;
//
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.ws.rs.Consumes;
// import jakarta.ws.rs.Produces;
// import jakarta.ws.rs.container.ContainerRequestContext;
// import jakarta.ws.rs.container.ContainerResponseContext;
// import jakarta.ws.rs.container.ContainerResponseFilter;
// import jakarta.ws.rs.core.Context;
// import jakarta.ws.rs.core.MediaType;
// import jakarta.ws.rs.ext.Provider;
// import lombok.extern.slf4j.Slf4j;
// import net.dusense.framework.core.launcher.web.ExceptionRespEntity;
// import net.dusense.framework.core.web.R;
// import org.springframework.stereotype.Component;
//
// import java.io.IOException;
// import java.util.Optional;
//
/// **
// * <p></p>
// *
// * @author [ saily ]
// * @version V3.0
// * @email sailyfirm@gmail.com
// * @date 2022/06/04
// **/
// @Slf4j
// @Provider
// @Consumes({ MediaType.APPLICATION_JSON, MediaType.WILDCARD })
// @Produces({ MediaType.APPLICATION_JSON, MediaType.WILDCARD })
//// @ConditionalOnProperty(value = "spring.jersey.application-path")
// @Component
// public class RespHandlerFilter implements ContainerResponseFilter {
//
//    @Context
//    private HttpServletRequest servletRequest;
//
//    @Override
//    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext
// containerResponseContext)
//            throws IOException {
//        if (Optional.ofNullable(containerResponseContext.getEntity()).isPresent() && (
//
// containerResponseContext.getEntityClass().isAssignableFrom(ExceptionRespEntity.class) ||
//
// containerResponseContext.getEntityClass().isAssignableFrom(Exception.class) ||
//                        containerResponseContext.getEntityClass().isAssignableFrom(Error.class)))
// {
//            log.error(
//                    servletRequest.getRemoteAddr() + ":" + servletRequest.getRemotePort() + " => "
// + servletRequest.getRequestURI() + " - " + servletRequest.getHeader(
//                            "sessionId"));
//            return;
//        } else if (Optional.ofNullable(containerResponseContext.getEntity()).isEmpty() ||
//                containerResponseContext.getEntityClass().isAssignableFrom(String.class)) {
//            log.info(
//                    servletRequest.getRemoteAddr() + ":" + servletRequest.getRemotePort() + " => "
// + servletRequest.getRequestURI() + " - " + servletRequest.getHeader(
//                            "sessionId"));
//
// containerResponseContext.setEntity(R.ok(containerResponseContext.getEntity()));
//        } else {
//
// containerResponseContext.setEntity(R.ok(containerResponseContext.getEntity()));
//
//        }
//    }
// }
