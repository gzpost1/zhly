package cn.cuiot.dmp.lease.entity.charge;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 收费管理-催款记录
 *
 * @author zc
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "tb_charge_collection_record")
public class ChargeCollectionRecordEntity extends YjBaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 企业ID
     */
    @TableField(value = "company_id")
    private Long companyId;

    /**
     * 客户ID
     */
    @TableField(value = "customer_user_id")
    private Long customerUserId;

    /**
     * 通知渠道（1:短信，2:微信）
     */
    @TableField(value = "channel")
    private Byte channel;

    /**
     * 催缴类型（1:手动催缴，2:计划催缴）
     */
    @TableField(value = "`type`")
    private Byte type;
}