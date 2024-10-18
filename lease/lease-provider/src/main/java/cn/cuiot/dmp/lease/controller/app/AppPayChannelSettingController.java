package cn.cuiot.dmp.lease.controller.app;

import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.lease.service.balance.MbRechargeOrderService;
import cn.cuiot.dmp.pay.service.service.service.SysPayChannelSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * app端收款商配置
 *
 * @author huq
 * @since 2022-06-07
 */
@Slf4j
@RestController
@RequestMapping("/app/setting/pay-channel-setting")
public class AppPayChannelSettingController {

    @Autowired
    private SysPayChannelSettingService settingService;

    @Autowired
    private MbRechargeOrderService mbRechargeOrderService;



    /**
     * 查询是否配置支付信息
     *
     * @return
     */
    @PostMapping("/queryHavePayInfo")
    public IdmResDTO<Boolean> queryAdminForDetail(@RequestBody @Valid IdParam param) {
        //如果有私钥 说明已经配置了支付信息
        return IdmResDTO.success(settingService.queryAdminForDetail(mbRechargeOrderService.quertOrgIdByHouse(param.getId())).getPriUpload());
    }

}
