package cn.cuiot.dmp.lease.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.base.application.enums.BeanValidationGroup;

/**
 * 租赁合同关联信息
 *
 * @author MJ~
 * @since 2024-07-03
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_contract_lease_relate")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TbContractLeaseRelateEntity extends Model<TbContractLeaseRelateEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "id不能为空", groups = BeanValidationGroup.Update.class)
    private Long id;

    /**
     * 合同id
     */
    private Long contractId;

    /**
     * 合同名称
     */
    private String name;

    /**
     * 关联时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime datetime;

    /**
     * 操作人
     */
    private Long operatorId;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 合同类型 1.意向合同 2.新生成续租合同的关联 3.该续租合同关联的原合同信息
     */
    private Integer type;

    /**
     * 关联id
     */
    private Long extId;

    private Byte status;

    /**
     * 关联原因
     */
    private String reason;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    public static final String TABLE_NAME = "tb_contract_lease_relate";


    public static final String ID = "id";

    public static final String CONTRACT_ID = "contract_id";

    public static final String NAME = "name";

    public static final String DATETIME = "datetime";

    public static final String OPERATOR_ID = "operator_id";

    public static final String OPERATOR = "operator";

    public static final String TYPE = "type";

    public static final String EXT_ID = "ext_id";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String CREATE_USER = "create_user";

    public static final String UPDATE_USER = "update_user";

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
