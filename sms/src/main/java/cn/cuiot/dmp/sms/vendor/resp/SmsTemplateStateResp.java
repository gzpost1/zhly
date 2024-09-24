package cn.cuiot.dmp.sms.vendor.resp;

import lombok.Data;

/**
 * 短信模板状态响应参数
 *
 * @Author: zc
 * @Date: 2024-09-23
 */
@Data
public class SmsTemplateStateResp {

    /**
     * 模板Id
     */
    private Integer id;

    /**
     * 审核状态：0 未审核，1 审核成功，2 审核拒绝
     */
    private Integer checkType;

    /**
     * 审核备注
     */
    private String checkRemark;
}
