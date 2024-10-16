package cn.cuiot.dmp.lease.controller.charge;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.pay.service.service.entity.TbPrePayAutoConfig;
import cn.cuiot.dmp.pay.service.service.service.TbPrePayAutoConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 预缴代扣设置
 *
 * @author libo
 */
@RestController
@RequestMapping("/prePayAutoConfig")
public class TbPrePayAutoConfigController {

    @Autowired
    private TbPrePayAutoConfigService tbPrePayAutoConfigService;


    /**
     * 获取详情
     *
     * @param idParam
     * @return
     */
    @RequiresPermissions
    @PostMapping("/queryForDetail")
    public IdmResDTO<TbPrePayAutoConfig> queryForDetail(@RequestBody IdParam idParam) {
        return IdmResDTO.success().body(tbPrePayAutoConfigService.getById(idParam.getId()));
    }


    /**
     * 更新
     *
     * @param updateDto
     * @return
     */
    @RequiresPermissions
    @PostMapping("/update")
    public IdmResDTO update(@RequestBody @Valid TbPrePayAutoConfig updateDto) {
        updateDto.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        tbPrePayAutoConfigService.saveOrUpdate(updateDto);
        return IdmResDTO.success();
    }


}

