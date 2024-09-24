package cn.cuiot.dmp.sms.query;

import lombok.Data;

/**
 * 第三方推送数据
 *
 * @Author: zc
 * @Date: 2024-09-23
 */
@Data
public class SmsPushDataQuery {

    /**
     * 任务Id，与发送短信时返回的任务Id相对应。
     */
    private String TaskId;

    /**
     * 自定义id
     */
    private String CustomId;

    /**
     * 手机号
     */
    private String Phone;

    /**
     * 报告状态（1 成功，2 失败，其他）
     */
    private Integer ReportStatus;

    /**
     * 报告描述
     */
    private String ReportDescription;

    /**
     * 报告时间
     */
    private String ReportTime;
}
