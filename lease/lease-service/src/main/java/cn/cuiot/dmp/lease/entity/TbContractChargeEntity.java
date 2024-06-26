package cn.cuiot.dmp.lease.entity;

import cn.cuiot.dmp.base.application.enums.BeanValidationGroup;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * 费用条款
 *
 * @author MJ~
 * @since 2024-06-24
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_contract_charge")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TbContractChargeEntity extends Model<TbContractChargeEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "id不能为空",groups = BeanValidationGroup.Update.class)
    private Long id;

    /**
     * 合同编号
     */
    private Long contractId;

    /**
     * 交费项目
     */
    private String project;

    /**
     * 交费房屋
     */
    private String house;

    /**
     * 首次交费日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate firstDate;

    /**
     * 缴费金额
     */
    private BigDecimal amount;

    /**
     * 收款方
     */
    private String payee;

    /**
     * 付款方
     */
    private String payer;

    @TableField(fill = FieldFill.INSERT)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    public static final String TABLE_NAME = "tb_contract_charge";


    public static final String ID = "id";

    public static final String CONTRACT_ID = "contract_id";

    public static final String PROJECT = "project";

    public static final String HOUSE = "house";

    public static final String FIRST_DATE = "first_date";

    public static final String AMOUNT = "amount";

    public static final String PAYEE = "payee";

    public static final String PAYER = "payer";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String CREATE_USER = "create_user";

    public static final String UPDATE_USER = "update_user";

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
