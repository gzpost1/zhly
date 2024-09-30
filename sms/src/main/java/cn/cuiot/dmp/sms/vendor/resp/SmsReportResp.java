package cn.cuiot.dmp.sms.vendor.resp;

import lombok.Data;

/**
 * 短信报告响应参数
 *
 * @Author: zc
 * @Date: 2024-09-20
 */
@Data
public class SmsReportResp {

    /**
     * 任务唯一标识
     */
    private String taskId;

    /**
     * 自定义标识
     */
    private String customId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 报告状态：0 未知，1 成功，2 失败，3其他，99 余额不足
     */
    private Integer reportStatus;

    /**
     * 报告描述
     */
    private String reportDescription;

    /**
     * 报告时间
     */
    private String reportTime;
}
