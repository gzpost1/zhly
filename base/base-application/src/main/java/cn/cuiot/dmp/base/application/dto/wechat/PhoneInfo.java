package cn.cuiot.dmp.base.application.dto.wechat;

import lombok.Data;

/**
 * 手机号信息
 */
@Data
public class PhoneInfo {
    /**
     * 用户绑定的手机号（国外手机号会有区号）
     */
    private String phoneNumber;
    /**
     * 没有区号的手机号
     */
    private String purePhoneNumber;
    /**
     * 区号
     */
    private String countryCode;
    /**
     * 数据水印
     */
    private Watermark watermark;
}
