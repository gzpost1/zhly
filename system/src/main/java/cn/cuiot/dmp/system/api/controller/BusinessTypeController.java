package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.system.application.param.vo.BusinessTypeVO;
import cn.cuiot.dmp.system.application.service.BusinessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author caorui
 * @date 2024/4/26
 */
@RestController
@RequestMapping("/businessType")
public class BusinessTypeController {

    @Autowired
    private BusinessTypeService businessTypeService;

    /**
     * 根据id获取业务类型详情
     */
    @PostMapping("/queryForDetail")
    public BusinessTypeVO queryForDetail(@RequestBody @Valid IdParam idParam){
        return businessTypeService.queryForDetail(idParam.getId());
    }

}
