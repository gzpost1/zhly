package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.common.annotation.RequiresPermissions;
import cn.cuiot.dmp.system.application.service.IndicatorCardService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zjb
 * @classname IndicatorCardController
 * @description 标签卡控制层
 * @date 2023/1/13
 */
@RestController
@RequestMapping("/indicatorCard")
public class IndicatorCardController extends BaseController {

    @Autowired
    private IndicatorCardService indicatorCardService;

    /**
     * 厂园区指标卡
     *
     * @return
     */
    @RequiresPermissions("basic:dashboard:console")
    @PostMapping(value = "/factoryPark", produces = MediaType.APPLICATION_JSON_VALUE)
    public  Map<String, Integer> factoryPark(){
        return indicatorCardService.factoryPark(getOrgId(), getUserId());
    }

}
