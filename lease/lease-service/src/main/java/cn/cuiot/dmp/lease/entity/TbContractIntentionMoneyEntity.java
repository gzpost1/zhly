package cn.cuiot.dmp.lease.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Map;

import lombok.*;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.NotNull;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.base.application.enums.BeanValidationGroup;
import org.springframework.data.annotation.Id;

/**
 * 意向金
 *
 * @author MJ~
 * @since 2024-06-12
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "tb_contract_intention_money",autoResultMap = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TbContractIntentionMoneyEntity extends Model<TbContractIntentionMoneyEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 收费项目
     */
    private String project;

    /**
     * 意向标的
     */
    private String bid;

    /**
     * 房号
     */
    @TableField(exist = false)
    private String roomNum;
    /**
     * 房屋编码
     */
    @TableField(exist = false)
    private String code;
    @TableField(exist = false)
    private String houseName;

    /**
     * 应收金额
     */
    private BigDecimal amount;

    /**
     * 备注
     */
    private String remark;

    private Long contractId;

    public static final String TABLE_NAME = "tb_contract_intention_money";


    public static final String ID = "id";

    public static final String PROJECT = "project";

    public static final String BID = "bid";

    public static final String AMOUNT = "amount";

    public static final String REMARK = "remark";

    @Override
    public Serializable pkVal() {
        return null;
    }

}
