package cn.cuiot.dmp.lease.entity.charge;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 收费标准
 */
@Data
@TableName(value = "tb_charge_standard")
public class TbChargeStandard extends YjBaseEntity {
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
     * 收费项目Id
     */
    @TableField(value = "charge_project_id")
    private Long chargeProjectId;

    /**
     * 收费标准
     */
    @TableField(value = "charge_standard")
    private String chargeStandard;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 是否启用 0否 1是
     */
    @TableField(value = "`status`")
    private Byte status;
}