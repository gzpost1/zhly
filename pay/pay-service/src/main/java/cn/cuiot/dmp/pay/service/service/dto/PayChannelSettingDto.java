package cn.cuiot.dmp.pay.service.service.dto;

import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.pay.service.service.enums.PayChannelEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author huq
 * @ClassName PayChannelSettingDto
 * @Date 2024/1/17 9:17
 **/
@Data
public class PayChannelSettingDto implements Serializable {
    /**
     * 支付渠道
     * 0：银联，1-微信、2-支付宝、3-工行、4-网付通、20-余额支付
     */
    @NotNull(message = "支付渠道不能为空")
    private Integer payChannel;
    /**
     * 商户模式
     * 3-默认商户或无商户模式，2-微信直付通、1-微信普通商户、0-微信普通服务商
     */
    @NotNull(message = "商户模式不能为空")
    private Byte mchType;
    /**
     * 商户号
     *
     */
    @NotBlank(message = "商户号不能为空")
    private String payMchId;

    /**
     * 商户名称名称
     */
    @NotBlank(message = "商户名称不能为空")
    private String payMchName;

    /**
     * 支付手续费
     */
    @NotNull(message = "支付手续费不能为空")
    private BigDecimal charge;

    /**
     * 私钥证书
     * 微信传apiclient_key.pem文件
     */
    @JsonIgnore
    private MultipartFile privateKeyFile;
    /**
     * 公钥证书
     */
    @JsonIgnore
    private MultipartFile publicKeyFile;

    /**
     * 其他支付参数
     * 微信的json格式为：
     * {"apiV3key":"xxx","mchSerialNo":"xxx"}
     */
    private String settingConfig;

    public void valid() {
        if (PayChannelEnum.isWeChat(this.payChannel)) {
            AssertUtil.notBlank(this.settingConfig, "支付参数不能为空");
            WeChatPayChannelSetting channelSetting = JsonUtil.readValue(this.settingConfig,
                    WeChatPayChannelSetting.class);
            AssertUtil.notBlank(channelSetting.getApiV3key(), "apiV3秘钥不能为空");
            AssertUtil.notBlank(channelSetting.getMchSerialNo(), "证书序列号不能为空");
        }
    }
}
