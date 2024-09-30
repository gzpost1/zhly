package cn.cuiot.dmp.sms.vendor.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 发送短信req
 *
 * @Author: zc
 * @Date: 2024-09-23
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class SmsSendReq {

    /**
     * 手机号码，多个号码请用英文逗号隔开，例：18300000000,1830000000
     */
    private String Mobile;

    /**
     * 短信内容，请保持在500字以内。注意：使用短信模板进行提交时，请填写模板拼接后的完整内容。
     */
    private String Content;

    /**
     * 短信模板ID
     */
    private String TemplateId;

    /**
     * 扩展号，由6位以内的数字组号
     */
    private String ExtCode;

    /**
     * 短信签名，例：【签名】，为空则自动识别已绑定的首个签名
     */
    private String SignName;

    /**
     * API定时时间，格式：yyyyMMddHHmmss
     */
    private String Timing;

    /**
     * 自定义ID，36位以内的字母数字组成
     */
    private String CustomId;
}
