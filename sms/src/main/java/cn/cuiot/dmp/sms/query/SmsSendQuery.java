package cn.cuiot.dmp.sms.query;

import lombok.Data;

/**
 * 短信发送query
 *
 * @Author: zc
 * @Date: 2024-09-23
 */
@Data
public class SmsSendQuery {

    /**
     * (必填)
     * 企业ID
     */
    private Long companyId;

    /**
     * (必填)
     * 手机号码，多个号码请用英文逗号隔开，例：18300000000,1830000000
     */
    private String mobile;

    /**
     * (必填)
     * 短信内容，请保持在500字以内。注意：使用短信模板进行提交时，请填写模板拼接后的完整内容。
     */
    private String content;

    /**
     * (必填)
     * 内部模板id
     */
    private Integer stdTemplate;
}
