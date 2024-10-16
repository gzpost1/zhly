package cn.cuiot.dmp.pay.service.service.vo;

import lombok.Data;

/**
 * @author huq
 * @ClassName WeChatConfig
 * @Date 2024/1/24 16:55
 **/
@Data
public class WeChatConfigVo {
    /**
     * 证书序列号
     */
    private String serialNo;
    private String apiV3key;

}
