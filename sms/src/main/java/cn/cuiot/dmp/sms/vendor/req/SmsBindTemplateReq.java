package cn.cuiot.dmp.sms.vendor.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 申请模板req
 *
 * @Author: zc
 * @Date: 2024-09-23
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class SmsBindTemplateReq {

    /**
     * 需要绑定的模板内容，模板中，变量请用“{s预期变量长度}”隔开。例：尊敬的{s10}您好，您的验证码是{s6}，有效时间{s1}分钟，请勿转发！
     */
    private String TemplateContent;
}
