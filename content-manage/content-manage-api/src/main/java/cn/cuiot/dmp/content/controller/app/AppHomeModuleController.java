package cn.cuiot.dmp.content.controller.app;//	模板

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 小程序首页模块控制器
 *
 * @author hantingyao
 * @Description
 * @data 2024/6/6 14:13
 */

@RestController
@RequestMapping("/app/appHomeModule")
public class AppHomeModuleController {

    @GetMapping("/getAppHomeModule")
    public void getAppHomeModule() {
        // TODO
    }
}
