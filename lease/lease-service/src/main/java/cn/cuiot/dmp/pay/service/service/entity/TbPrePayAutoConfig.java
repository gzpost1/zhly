package cn.cuiot.dmp.pay.service.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.cuiot.dmp.base.infrastructure.persistence.handler.ListObjectJsonTypeHandler;
import lombok.Data;

import java.util.List;

/**
 * 预缴代扣设置
 */
@Data
@TableName(value = "tb_pre_pay_auto_config", autoResultMap = true)
public class TbPrePayAutoConfig {
    /**
     * 企业id
     */
    @TableId(value = "company_id", type = IdType.INPUT)
    private Long companyId;

    /**
     * 是否自动扣款 0否 1是
     */
    @TableField(value = "is_auto_pay")
    private Byte isAutoPay;

    /**
     * 收费项目id
     */
    @TableField(value = "charge_item_ids",typeHandler = ListObjectJsonTypeHandler.class )
    private List<Long> chargeItemIds;
}