package cn.cuiot.dmp.pay.service.service.config;

import com.chinaunicom.yunjingtech.httpclient.auth.AutoUpdateCertificatesVerifier;
import lombok.Data;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

/**
 * @author huq
 * @ClassName WeChatConfig
 * @Date 2024/1/24 16:55
 **/
@Data
public class WeChatConfig {
    /**
     * 证书序列号
     */
    private String mchSerialNo;
    private String apiV3key;
    /**
     * 私钥证书
     */
    private byte[] privateKey;
    /**
     * 服务商支付号
     */
    private String payMchId;

    private String appId;
    /**
     * 支付回调地址
     */
    private String payNotify;
    /**
     * 退款回调地址
     */
    private String refundNotify;
    /**
     * 服务商名称
     */
    private String payMchName;

    /**
     * 支付手续费费率
     */
    private BigDecimal charge;
}
