package cn.cuiot.dmp.sms.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短信签名信息
 *
 * @Author: zc
 * @Date: 2024-09-20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "tb_sms_sign")
public class SmsSignEntity extends YjBaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 企业id
     */
    @TableField(value = "company_id")
    private Long companyId;

    /**
     * 租户类型
     */
    @TableField(value = "org_type_id")
    private Long orgTypeId;

    /**
     * 签名
     */
    @TableField(value = "sign")
    private String sign;

    /**
     * 第三方签名编号
     */
    @TableField(value = "third_code")
    private Integer thirdCode;

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

    /**
     * 启停用状态（0：停用；1：启用）
     */
    private Byte status;

    private static final long serialVersionUID = 1L;
}