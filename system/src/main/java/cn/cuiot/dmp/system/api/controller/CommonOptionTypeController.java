package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.system.application.param.vo.CommonOptionTypeVO;
import cn.cuiot.dmp.system.application.service.CommonOptionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author caorui
 * @date 2024/4/28
 */
@RestController
@RequestMapping("/commonOptionType")
public class CommonOptionTypeController {

    @Autowired
    private CommonOptionTypeService commonOptionTypeService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public CommonOptionTypeVO queryForDetail(@RequestBody @Valid IdParam idParam){
        return commonOptionTypeService.queryForDetail(idParam.getId());
    }

}
