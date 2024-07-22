package cn.cuiot.dmp.lease.entity;

import cn.cuiot.dmp.base.application.enums.ContractEnum;
import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.model.HousesArchivesVo;
import cn.cuiot.dmp.common.constant.EntityConstants;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 意向合同
 *
 * @author MJ~
 * @since 2024-06-12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "tb_contract_intention", autoResultMap = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TbContractIntentionEntity extends BaseContractEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 租赁合同id
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Long contractLeaseId;



    /**
     * 跟进人
     */
    @NotNull(message = "跟进人不能为空")
    private String followUp;
    @TableField(exist = false)
    private String followUpName;

    /**
     * 签订客户
     */
    private String client;
    @TableField(exist = false)
    private String clientName;
    @TableField(exist = false)
    private String clientPhone;

    /**
     * 意向备注
     */
    private String remark;


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

    @TableField(fill = FieldFill.INSERT)
    private Byte status;

    @TableLogic
    @Builder.Default
    @TableField(fill = FieldFill.INSERT)
    private Byte deleted = EntityConstants.NOT_DELETED;



    @Override
    public void setContractStatus(Integer contractStatus) {
        if (Objects.equals(contractStatus, ContractEnum.STATUS_SIGNED.getCode())&&Objects.isNull(getCantractDate())) {
            LocalDate now = LocalDate.now();
            setCantractDate(now);
        }
        super.setContractStatus(contractStatus);
    }

    /**
     * 标签
     */
    private String label;

    public static final String TABLE_NAME = "tb_contract_intention";


    public static final String ID = "id";

    public static final String CONTRACT_NO = "contract_no";

    public static final String NAME = "name";

    public static final String CANTRACT_DATE = "cantract_date";

    public static final String BEGIN_DATE = "begin_date";

    public static final String END_DATE = "end_date";

    public static final String FOLLOW_UP = "follow_up";

    public static final String CLIENT = "client";

    public static final String REMARK = "remark";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String CREATE_USER = "create_user";

    public static final String UPDATE_USER = "update_user";

    public static final String STATUS = "status";

    public static final String DELETED = "deleted";

    public static final String CONTRACT_STATUS = "contract_status";

    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
