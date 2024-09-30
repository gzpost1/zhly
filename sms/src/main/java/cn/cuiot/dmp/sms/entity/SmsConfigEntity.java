package cn.cuiot.dmp.sms.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短信密钥
 *
 * @Author: zc
 * @Date: 2024-09-19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "tb_sms_config")
public class SmsConfigEntity extends YjBaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 密钥名称
     */
    @TableField(value = "secret_name")
    private String secretName;

    /**
     * 密钥key
     */
    @TableField(value = "secret_key")
    private String secretKey;

    private static final long serialVersionUID = 1L;
}