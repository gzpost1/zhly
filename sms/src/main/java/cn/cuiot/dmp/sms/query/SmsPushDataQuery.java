package cn.cuiot.dmp.sms.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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
    @JsonProperty("ReportStatus")
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
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
    private Date reportTime;
}
