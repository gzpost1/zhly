package cn.cuiot.dmp.pay.controller;

import cn.cuiot.dmp.pay.service.service.config.NormalWeChatConfig;
import cn.cuiot.dmp.pay.service.service.service.OrderPayAtHandler;
import cn.cuiot.dmp.pay.service.service.utils.WxSignUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.chinaunicom.yunjingtech.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.chinaunicom.yunjingtech.httpclient.bean.pay.request.CombinePayNotifyReq;
import com.chinaunicom.yunjingtech.httpclient.bean.pay.response.PaySuccessNotifyRsp;
import com.chinaunicom.yunjingtech.httpclient.bean.pay.response.normal.NormalQueryOrderResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * @author oujiangping
 */
@RestController
@RequestMapping(path = "pay/pay-notify")
@Slf4j
public class WePayNotifyController {

    @Autowired
    @Qualifier("NormalCertificatesVerifier")
    private AutoUpdateCertificatesVerifier verifier;

    @Autowired
    private NormalWeChatConfig weChatConfig;

    @Autowired
    private OrderPayAtHandler orderPayAtHandler;
    /**
     * 普通商户成功通知
     */
    @RequestMapping(path = "/notifyPayNotify", method = RequestMethod.POST)
    public PaySuccessNotifyRsp notifyPayNotify(@RequestBody String jsonStr,
                                               HttpServletRequest request,
                                               HttpServletResponse response) {
        log.info("支付成功通知消息：" + jsonStr);
        // 验签
        WxSignUtil.checkSign(request, jsonStr, response, verifier);
        // 获取最终应答数据
        String wechatResourceStringData = WxSignUtil.getWechatResourceStringData(JSON.parseObject(jsonStr, CombinePayNotifyReq.class), weChatConfig.getApiV3key());
        orderPayAtHandler.onReceiverWechatNormalPayNotify(wechatResourceStringData);
        return PaySuccessNotifyRsp.builder().code("SUCCESS").build();
    }


}
