package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.system.application.param.vo.FormConfigTypeVO;
import cn.cuiot.dmp.system.application.service.FormConfigTypeService;
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
@RequestMapping("/formConfigType")
public class FormConfigTypeController {

    @Autowired
    private FormConfigTypeService formConfigTypeService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public FormConfigTypeVO queryForDetail(@RequestBody @Valid IdParam idParam){
        return formConfigTypeService.queryForDetail(idParam.getId());
    }

}
