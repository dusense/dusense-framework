package net.dusense.framework.core.launcher.facade;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Description: [描述该类概要功能介绍]
 *
 * @version $Revision$
 * @author:[Fan Yang]
 */
@FeignClient(value = "dusense-sysmanage", path = "/sysmanage/syslog")
public interface SysLogFacade {

    /** 保存log */
    @RequestMapping(method = RequestMethod.POST)
    public String saveObj(@RequestBody SysLog sysLog);
}
