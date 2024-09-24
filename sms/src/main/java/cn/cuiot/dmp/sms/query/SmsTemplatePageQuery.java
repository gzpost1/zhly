package cn.cuiot.dmp.sms.query;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短信模板 pageQuery
 *
 * @Author: zc
 * @Date: 2024-09-20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SmsTemplatePageQuery extends PageQuery {

    /**
     * 标准模版id
     */
    private Integer stdTemplate;

    /**
     * 第三方模版id
     */
    private String thirdTemplate;

    /**
     * 审核状态：0 未审核，1 审核成功，2 审核拒绝
     */
    private Integer thirdStatus;

    /**
     * 模版类型：1 验证码，2 公告通知，3 线索通知，4 工单通知，5 催缴通知，6 账单通知
     */
    private Byte smsType;
}
