package cn.cuiot.dmp.pay.service.service.normal;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.pay.service.service.config.NormalWeChatConfig;
import cn.cuiot.dmp.pay.service.service.config.PayHttpClient;
import cn.cuiot.dmp.pay.service.service.dto.UnifiedOrderResponseDto;
import cn.cuiot.dmp.pay.service.service.dto.WePayOrderQuery;
import cn.cuiot.dmp.pay.service.service.utils.WechatSignTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.chinaunicom.yunjingtech.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.chinaunicom.yunjingtech.httpclient.bean.pay.request.normal.NormalCloseOrderReq;
import com.chinaunicom.yunjingtech.httpclient.bean.pay.request.normal.NormalOrderReq;
import com.chinaunicom.yunjingtech.httpclient.bean.pay.response.normal.NormalQueryOrderResp;
import com.chinaunicom.yunjingtech.httpclient.util.AesUtil;
import com.chinaunicom.yunjingtech.httpclient.util.PemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.security.PrivateKey;

/**
 * @author huq
 * @ClassName NormalPayService
 * @Date 2021/7/20 10:54
 **/
@Slf4j
@Service
public class NormalPayHttpService  implements PaySettingInit{
    private static final String PAY_URL = "https://api.mch.weixin.qq.com/v3/pay/transactions/{payMethod}";
    private static final String QUERY_URL = "https://api.mch.weixin.qq" +
            ".com/v3/pay/transactions/out-trade-no/{out_trade_no}";
    private static final String CLOSE_URL = "https://api.mch.weixin.qq" +
            ".com/v3/pay/transactions/out-trade-no/{out_trade_no}/close";

    private PayHttpClient httpClient;

    @Autowired
    private NormalWeChatConfig weChatConfig;

    @Autowired
    @Qualifier("NormalCertificatesVerifier")
    private AutoUpdateCertificatesVerifier verifier;

    @Override
    public void init() {
        httpClient = PayHttpClient.build(weChatConfig, verifier);
    }
    /**
     * 功能描述: 下预付单
     *
     * @param query
     * @return com.yunjingtech.sot.pay.vo.pay.UnifiedOrderRspFild
     * @author huq
     * @date 2021/7/20 11:44
     **/
    public UnifiedOrderResponseDto payOrder(NormalOrderReq query, String payMethod) {

        String url = PAY_URL.replace("{payMethod}", payMethod);
        query.setNotifyUrl(weChatConfig.getPayNotify().replace("{mchId}", weChatConfig.getPayMchId() + ""));
        String jsonStr = httpClient.requestHttpPostObj(url, query, "下单失败");
        JSONObject json = JSON.parseObject(jsonStr);
        String prepayId = json.get("prepay_id").toString();
        UnifiedOrderResponseDto responseDto = new UnifiedOrderResponseDto();
        responseDto.setAppId(query.getAppId());
        responseDto.setNonceStr(WechatSignTool.getNoceStr(30));
        responseDto.setPkg("prepay_id=" + prepayId);
        responseDto.setSignType("RSA");
        responseDto.setTimeStamp(System.currentTimeMillis() / 1000 + "");
        String msg = WechatSignTool.buildMessage(responseDto.getAppId(), responseDto.getTimeStamp(),
                responseDto.getNonceStr(), responseDto.getPkg());
        PrivateKey privateKey = null;
        try {
            privateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(weChatConfig.getPrivateKey()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        responseDto.setPaySign(AesUtil.sign(msg.getBytes(), privateKey));
        return responseDto;
    }

    /**
     * 功能描述: 查询订单
     *
     * @param orderQuery
     * @author huq
     * @date 2021/7/26 11:43
     **/
    public NormalQueryOrderResp queryOrder(WePayOrderQuery orderQuery) {
        try {
            String url = QUERY_URL.replace("{out_trade_no}", orderQuery.getOutTradeNo());
            url = url + String.format("?mchid=%s", orderQuery.getPayMchId());
            String responseStr = httpClient.requestHttpGetObj(url, "查单失败");
            return JSON.parseObject(responseStr,
                    new TypeReference<NormalQueryOrderResp>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("私钥文件不存在:" + e.getMessage());
            throw new BusinessException(ResultCode.WECHAT_PAY_RESULT_ERROR, "微信查单失败：" + e.getMessage());
        }
    }

    /**
     * 关单
     *
     * @param outTradeNo
     * @return
     */
    public Boolean closeOrder(String outTradeNo) {

        String url = CLOSE_URL.replace("{out_trade_no}", outTradeNo);
        NormalCloseOrderReq closeOrderReq = NormalCloseOrderReq.builder().mchId(weChatConfig.getPayMchId()).build();
        httpClient.requestHttpPostNoReturn(url, closeOrderReq, "关单失败");
        return true;
    }


}
