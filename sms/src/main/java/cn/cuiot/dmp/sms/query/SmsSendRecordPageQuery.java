package cn.cuiot.dmp.sms.query;

import cn.cuiot.dmp.common.bean.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 短信发送记录分页查询query
 *
 * @Author: zc
 * @Date: 2024-09-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SmsSendRecordPageQuery extends PageQuery {

    /**
     * 手机号
     */
    private String phone;

    /**
     * 发送开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginDate;

    /**
     * 发送结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endDate;

    /**
     * 发送状态（1 成功，2 失败，其他）
     */
    private Integer reportStatus;

    /**
     * 模版类型：1 验证码，2 公告通知，3 线索通知，4 工单通知，5 催缴通知，6 账单通知
     */
    private Byte smsType;
}
