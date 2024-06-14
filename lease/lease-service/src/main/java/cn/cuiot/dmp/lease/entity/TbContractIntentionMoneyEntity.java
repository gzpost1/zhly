package cn.cuiot.dmp.lease.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.NotNull;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.base.application.enums.BeanValidationGroup;
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
@TableName("tb_contract_intention_money")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TbContractIntentionMoneyEntity extends Model<TbContractIntentionMoneyEntity> {

    private static final long serialVersionUID = 1L;

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
     * 应收金额
     */
    private BigDecimal amount;

    /**
     * 备注
     */
    private String remark;

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
