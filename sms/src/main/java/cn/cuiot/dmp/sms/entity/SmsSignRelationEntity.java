package cn.cuiot.dmp.sms.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短信-发送签名设置
 *
 * @Author: zc
 * @Date: 2024-09-20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "tb_sms_sign_relation")
public class SmsSignRelationEntity extends YjBaseEntity {
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
     * 签名id
     */
    @TableField(value = "sign_id")
    private Long signId;

    /**
     * 类型：0 默认，1 企业
     */
    @TableField(value = "`type`")
    private Byte type;

    private static final long serialVersionUID = 1L;
}