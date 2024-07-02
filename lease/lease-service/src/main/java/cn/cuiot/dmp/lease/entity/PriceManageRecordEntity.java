package cn.cuiot.dmp.lease.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author caorui
 * @date 2024/6/21
 */
@Data
@TableName(value = "price_manage_record", autoResultMap = true)
@NoArgsConstructor
@AllArgsConstructor
public class PriceManageRecordEntity implements Serializable {

    private static final long serialVersionUID = -3316730471784073621L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 定价管理编码
     */
    private Long priceId;

    /**
     * 操作名称
     */
    private String operateName;

    /**
     * 操作人id
     */
    private Long operatorId;

    /**
     * 操作人名称
     */
    @TableField(exist = false)
    private String operatorName;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operateTime;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 作废备注
     */
    private String invalidRemark;

    /**
     * 逻辑删除，1已删除，0未删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore
    private Integer deletedFlag;

    public PriceManageRecordEntity(Long priceId, String operateName, Long operatorId, Date operateTime,
                                   String auditRemark, String invalidRemark) {
        this.priceId = priceId;
        this.operateName = operateName;
        this.operatorId = operatorId;
        this.operateTime = operateTime;
        this.auditRemark = auditRemark;
        this.invalidRemark = invalidRemark;
    }

}
