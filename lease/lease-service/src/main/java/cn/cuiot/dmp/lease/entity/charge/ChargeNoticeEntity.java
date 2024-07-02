package cn.cuiot.dmp.lease.entity.charge;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 收费管理-通知单
 *
 * @author zc
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "tb_charge_notice")
public class ChargeNoticeEntity extends YjBaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 所属账期-开始时间
     */
    @TableField(value = "ownership_period_begin")
    private Date ownershipPeriodBegin;

    /**
     * 所属账期-结束时间
     */
    @TableField(value = "ownership_period_end")
    private Date ownershipPeriodEnd;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 停启用状态（0停用，1启用）
     */
    @TableField(value = "status")
    private Byte status;

    /**
     * 企业ID
     */
    @TableField(value = "company_id")
    private Long companyId;
}