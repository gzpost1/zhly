package cn.cuiot.dmp.pay.controller;

import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.pay.service.service.entity.SysPayChannelSetting;
import cn.cuiot.dmp.pay.service.service.service.CertificatesVerifierCacheService;
import cn.cuiot.dmp.pay.service.service.service.OrderPayAtHandler;
import cn.cuiot.dmp.pay.service.service.service.SysPayChannelSettingService;
import cn.cuiot.dmp.pay.service.service.utils.WxSignUtil;
import cn.cuiot.dmp.pay.service.service.vo.WeChatConfigVo;
import com.alibaba.fastjson.JSON;
import com.chinaunicom.yunjingtech.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.chinaunicom.yunjingtech.httpclient.bean.pay.request.CombinePayNotifyReq;
import com.chinaunicom.yunjingtech.httpclient.bean.pay.response.PaySuccessNotifyRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author oujiangping
 */
@RestController
@RequestMapping(path = "pay/pay-notify")
@Slf4j
public class WePayNotifyController {


    @Autowired
    private OrderPayAtHandler orderPayAtHandler;

    @Autowired
    private SysPayChannelSettingService settingService;

    @Autowired
    private CertificatesVerifierCacheService cacheService;
    /**
     * 普通商户成功通知
     */
    @RequestMapping(path = "/{orgId}/notifyPayNotify", method = RequestMethod.POST)
    public PaySuccessNotifyRsp notifyPayNotify(@RequestBody String jsonStr,@PathVariable("orgId") String orgId,
                                               HttpServletRequest request,
                                               HttpServletResponse response) {
        log.info("支付成功通知消息：" + jsonStr);
        // 验签
        SysPayChannelSetting paySetting = settingService.getPaySettingByOrgId(Long.parseLong(orgId));
        AutoUpdateCertificatesVerifier certificatesVerifier = cacheService.getAutoUpdateCertificatesVerifierByPayMchId(paySetting);
        WxSignUtil.checkSign(request, jsonStr, response, certificatesVerifier);
        // 获取最终应答数据
        WeChatConfigVo config = JsonUtil.readValue(paySetting.getSettingConfig(), WeChatConfigVo.class);
        String wechatResourceStringData = WxSignUtil.getWechatResourceStringData(JSON.parseObject(jsonStr, CombinePayNotifyReq.class), config.getApiV3key());
        orderPayAtHandler.onReceiverWechatNormalPayNotify(wechatResourceStringData);
        return PaySuccessNotifyRsp.builder().code("SUCCESS").build();
    }


}
