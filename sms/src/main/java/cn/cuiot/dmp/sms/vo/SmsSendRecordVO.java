package cn.cuiot.dmp.sms.vo;

import lombok.Data;

import java.util.Date;

/**
 * 短信发送记录
 *
 * @Author: zc
 * @Date: 2024-09-23
 */
@Data
public class SmsSendRecordVO {

    /**
     * id
     */
    private Long id;

    /**
     * 任务唯一标识
     */
    private String taskId;

    /**
     * 短信内容
     */
    private String content;

    /**
     * 短信签名
     */
    private String sign;

    /**
     * 模版类型：1 验证码，2 公告通知，3 线索通知，4 工单通知，5 催缴通知，6 账单通知
     */
    private Byte smsType;

    /**
     * 报告状态：0 未知，1 成功，2 失败，3其他，99 余额不足
     */
    private String reportStatus;

    /**
     * 报告描述
     */
    private String reportDescription;

    /**
     * 报告时间
     */
    private Date reportTime;

    /**
     * 手机号
     */
    private String phone;
}
