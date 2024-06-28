package cn.cuiot.dmp.content.controller;//	模板

import cn.cuiot.dmp.base.application.annotation.InternalApi;
import cn.cuiot.dmp.content.service.ContentModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/19 11:40
 */
@RequestMapping("/api")
@InternalApi
@RestController
public class ApiController {

    @Autowired
    private ContentModuleService contentModuleService;

    /**
     * 初始化模块
     *
     * @param orgId
     * @return
     */
    @PostMapping("/initModule")
    public Boolean initModule(@RequestParam("orgId") Long orgId) {
        contentModuleService.initModule(orgId);
        return true;
    }
}
