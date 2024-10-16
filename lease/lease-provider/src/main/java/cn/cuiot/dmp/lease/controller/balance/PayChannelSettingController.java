package cn.cuiot.dmp.lease.controller.balance;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.pay.service.service.dto.PayChannelSettingDto;
import cn.cuiot.dmp.pay.service.service.service.SysPayChannelSettingService;
import cn.cuiot.dmp.pay.service.service.vo.SysPayChannelSettingDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 收款商配置
 *
 * @author huq
 * @since 2022-06-07
 */
@Slf4j
@RestController
@RequestMapping("/setting/pay-channel-setting")
public class PayChannelSettingController {

    @Autowired
    private SysPayChannelSettingService settingService;



    /**
     * 查询系统设置的支付参数详情
     *
     * @return
     */
    @RequiresPermissions
    @PostMapping("/queryAdminForDetail")
    public IdmResDTO<SysPayChannelSettingDetailVo> queryAdminForDetail() {
        return IdmResDTO.success(settingService.queryAdminForDetail());
    }

    /**
     * 更新支付渠道新增
     *
     * @param param
     */
    @RequiresPermissions
    @PostMapping("/update")
    public IdmResDTO update(@Valid PayChannelSettingDto param) {
        settingService.updatePaySetting(param);
        return IdmResDTO.success();
    }

}
