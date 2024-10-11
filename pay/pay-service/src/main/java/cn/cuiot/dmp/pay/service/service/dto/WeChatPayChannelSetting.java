package cn.cuiot.dmp.pay.service.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author huq
 * @ClassName WeChatPayChannelSetting
 * @Date 2024/1/17 14:36
 **/
@Data
public class WeChatPayChannelSetting implements Serializable {
    /**
     * apiV3秘钥
     */
    private String apiV3key;
    /**
     * 证书序列号
     */
    private String mchSerialNo;
}
