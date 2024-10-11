package cn.cuiot.dmp.pay.service.service.utils;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import com.chinaunicom.yunjingtech.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.chinaunicom.yunjingtech.httpclient.bean.pay.request.CombinePayNotifyReq;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

/**
 * 微信特有工具类
 *
 * @author huq
 * @ClassName WxSignUtil
 * @Date 2024/1/29 11:10
 **/
@Slf4j
public class WxSignUtil {
    private static final String WECHAT_SIGN_HEADER = "Wechatpay-Signature";

    /**
     * 获取最终的应答数据
     */
    public static String getWechatResourceStringData(CombinePayNotifyReq param, String apiV3Key) {
        try {
            // 获取服务商支付APIV3KEY
            byte[] aesKeys = apiV3Key.getBytes(StandardCharsets.UTF_8);
            // 获取微信传过来的附加数据
            byte[] associatedData = param.getResource().getAssociatedData().getBytes(StandardCharsets.UTF_8);
            // 获取微信传过来的加密使用的随机串
            byte[] nonce = param.getResource().getNonce().getBytes(StandardCharsets.UTF_8);
            // 将微信传过来的resource数据解密,得到最终的应答数据
            String decrStr = WechatSignTool.decryptAes256ToString(aesKeys, associatedData,
                    nonce, param.getResource().getCiphertext());
            log.info("微信支付/退款成功通知：" + decrStr);
            return decrStr;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            log.warn("微信通知解密失败");
            e.printStackTrace();
        } catch (IOException e) {
            log.warn("微信通知解密失败");
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 使用平台私钥证书验证签名
     */
    public static void checkSign(HttpServletRequest request, String jsonStr, HttpServletResponse response,
                                 AutoUpdateCertificatesVerifier certificatesVerifier) {
        // 应答签名
        String sign = request.getHeader(WECHAT_SIGN_HEADER);
        // 微信支付平台证书
        String serialNo = request.getHeader("Wechatpay-Serial");
        // 时间戳
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        // 应答随机串
        String nonceStr = request.getHeader("Wechatpay-Nonce");
        String message = timestamp + "\n"
                + nonceStr + "\n"
                + jsonStr + "\n";
        log.info("头部带的参数：" + message + ",sign：" + sign + ",serialNo：" + serialNo);
        // 验证签名
        if (!certificatesVerifier.verify(serialNo, message.getBytes(StandardCharsets.UTF_8), sign)) {
            response.setStatus(401);
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "不是微信来源信息");
        }
    }
}
