package cn.cuiot.dmp.sms.query;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("TaskId")
    private String taskId;

    /**
     * 自定义id
     */
    @JsonProperty("CustomId")
    private String customId;

    /**
     * 手机号
     */
    @JsonProperty("Phone")
    private String phone;

    /**
     * 报告状态（1 成功，2 失败，其他）
     */
    @JsonProperty("reportStatus")
    private Integer reportStatus;

    /**
     * 报告描述
     */
    @JsonProperty("ReportDescription")
    private String reportDescription;

    /**
     * 报告时间
     */
    @JsonProperty("ReportTime")
    private String reportTime;
}
