package cn.cuiot.dmp.pay.controller;

import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.pay.service.service.dto.PayChannelSettingDto;
import cn.cuiot.dmp.pay.service.service.service.SysPayChannelSettingService;
import cn.cuiot.dmp.pay.service.service.vo.SysPayChannelSettingDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 交易中心-支付渠道管理
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
    @PostMapping("/queryAdminForDetail")
    public IdmResDTO<SysPayChannelSettingDetailVo> queryAdminForDetail() {
        return IdmResDTO.success(settingService.queryAdminForDetail());
    }

    /**
     * 更新支付渠道新增
     *
     * @param param
     */
    @PostMapping("/update")
    public IdmResDTO update(@Valid PayChannelSettingDto param) {
        settingService.updatePaySetting(param);
        return IdmResDTO.success();
    }

}
