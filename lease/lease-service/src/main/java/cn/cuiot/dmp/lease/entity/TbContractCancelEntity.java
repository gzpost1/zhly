package cn.cuiot.dmp.lease.entity;

import cn.cuiot.dmp.base.application.enums.BeanValidationGroup;
import cn.cuiot.dmp.common.constant.EntityConstants;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * 退订信息
 *
 * @author MJ~
 * @since 2024-06-17
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_contract_cancel")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TbContractCancelEntity extends Model<TbContractCancelEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "id不能为空",groups = BeanValidationGroup.Update.class)
    private Long id;

    /**
     * 意向合同ID
     */
    private Long intentionContractId;
    /**
     * 租赁合同ID
     */
    private Long leaseContractId;

    /**
     * 合同名称
     */
    @NotNull(message = "合同名称不能为空")
    private String name;

    /**
     * 退订日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate date;

    /**
     * 退订原因
     */
    private String reason;

    /**
     * 退订说明 或 作废备注
     */
    private String remark;

    /**
     * 退订附件
     */
    private String path;

    /**
     * 0.退订 1.作废
     */
    private Integer type;

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

    @TableField(fill = FieldFill.INSERT)
    private Byte status;

    @TableLogic
    @Builder.Default
    @TableField(fill = FieldFill.INSERT)
    private Byte deleted = EntityConstants.NOT_DELETED;

    public static final String TABLE_NAME = "tb_contract_cancel";


    public static final String ID = "id";

    public static final String CONTRACT_NO = "contract_no";

    public static final String NAME = "name";

    public static final String DATE = "date";

    public static final String REASON = "reason";

    public static final String REMARK = "remark";

    public static final String PATH = "path";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String CREATE_USER = "create_user";

    public static final String UPDATE_USER = "update_user";

    public static final String STATUS = "status";

    public static final String DELETED = "deleted";

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
