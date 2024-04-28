package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.system.application.param.dto.BusinessTypeCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.BusinessTypeUpdateDTO;
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
     * 根据id获取详情
     */
    @RequiresPermissions
    @PostMapping("/queryForDetail")
    public BusinessTypeVO queryForDetail(@RequestBody @Valid IdParam idParam){
        return businessTypeService.queryForDetail(idParam.getId());
    }

    /**
     * 创建
     */
    @RequiresPermissions
    @PostMapping("/create")
    public int create(@RequestBody @Valid BusinessTypeCreateDTO businessTypeCreateDTO){
        return businessTypeService.saveBusinessType(businessTypeCreateDTO);
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @PostMapping("/update")
    public int update(@RequestBody @Valid BusinessTypeUpdateDTO businessTypeUpdateDTO){
        return businessTypeService.updateBusinessType(businessTypeUpdateDTO);
    }

}
