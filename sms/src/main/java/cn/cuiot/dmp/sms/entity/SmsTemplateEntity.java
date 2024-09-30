package cn.cuiot.dmp.sms.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短信模版信息
 *
 * @Author: zc
 * @Date: 2024-09-20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "tb_sms_template")
public class SmsTemplateEntity extends YjBaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标准模版id
     */
    @TableField(value = "std_template")
    private Integer stdTemplate;

    /**
     * 第三方模版id
     */
    @TableField(value = "third_template")
    private Integer thirdTemplate;

    /**
     * 模版内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 模版类型：1 验证码，2 公告通知，3 线索通知，4 工单通知，5 催缴通知，6 账单通知
     */
    @TableField(value = "sms_type")
    private Byte smsType;

    /**
     * 审核状态：0 未审核，1 审核成功，2 审核拒绝
     */
    @TableField(value = "third_status")
    private Integer thirdStatus;

    /**
     * 审核备注
     */
    @TableField(value = "remark")
    private String remark;

    private static final long serialVersionUID = 1L;
}