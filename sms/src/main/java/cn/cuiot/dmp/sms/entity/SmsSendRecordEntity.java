package cn.cuiot.dmp.sms.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 短信发送记录
 *
 * @Author: zc
 * @Date: 2024-09-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Document("tb_sms_send_record")
public class SmsSendRecordEntity extends YjBaseEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 企业ID
     */
    @TableField(value = "company_id")
    private Long companyId;

    /**
     * 第三方模版id
     */
    @TableField(value = "third_template")
    private Integer thirdTemplate;

    /**
     * 内容计费（条）
     */
    @TableField(value = "content_charging")
    private Integer contentCharging;

    /**
     * 部门编码路径
     */
    @TableField(value = "dept_path")
    private String deptPath;

    /**
     * 签名
     */
    @TableField(value = "sign")
    private String sign;

    /**
     * 任务唯一标识
     */
    @TableField(value = "task_id")
    private String taskId;

    /**
     * 自定义标识
     */
    @TableField(value = "custom_id")
    private String customId;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 模版类型：1 验证码，2 公告通知，3 线索通知，4 工单通知，5 催缴通知，6 账单通知
     */
    @TableField(value = "sms_type")
    private Byte smsType;

    /**
     * 报告状态：0 未知，1 成功，2 失败，3其他，99 余额不足
     */
    @TableField(value = "report_status")
    private Integer reportStatus;

    /**
     * 报告描述
     */
    @TableField(value = "report_description")
    private String reportDescription;

    /**
     * 短信内容，请保持在500字以内。注意：使用短信模板进行提交时，请填写模板拼接后的完整内容。
     */
    @TableField(value = "content")
    private String content;

    /**
     * 报告时间
     */
    @TableField(value = "report_time")
    private Date reportTime;

    public static final String COMPANY_ID = "company_id";
    public static final String PHONE = "phone";
    public static final String CREATE_TIME = "createTime";
    public static final String REPORT_STATUS = "report_status";
    public static final String SMS_TYPE = "sms_type";
    public static final String TASK_ID = "task_id";
    public static final String REPORT_DESCRIPTION = "report_description";
    public static final String REPORT_TIME = "report_time";
    public static final String DEPT_PATH = "dept_path";
}
