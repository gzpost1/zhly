package cn.cuiot.dmp.lease.entity;

import cn.cuiot.dmp.base.application.enums.BeanValidationGroup;
import cn.cuiot.dmp.base.application.enums.ContractEnum;
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
 * 租赁合同
 *
 * @author MJ~
 * @since 2024-06-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_contract_lease")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
public class TbContractLeaseEntity extends BaseContractEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 合同名称
     */
    @NotNull(message = "合同名称不能为空")
    private String name;


    /**
     * 首期应收日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate firstDate;

    /**
     * 跟进人
     */
    @NotNull(message = "跟进人不能为空")
    private String followUp;
    @TableField(exist = false)
    private String followUpName;


    /**
     * 租赁用途
     */
    @NotNull(message = "租赁用途不能为空")
    private String purpose;

    /**
     * 合同类型
     */
    @NotNull(message = "合同类型不能为空")
    private String type;

    /**
     * 合同性质
     */
    @NotNull(message = "合同性质不能为空")
    private String property;


    /**
     * 标签
     */
    private String label;

    /**
     * 备注
     */
    private String remark;

    /**
     * 签订客户
     */
    private String client;

    /**
     * 合同表单
     */
    private String form;
    /**
     * 合同表单值
     */
    private String formData;

    /**
     * 合同主体
     */
    private String mainBody;

    /**
     * 其他功能-附件
     */
    private String path;

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

    @Override
    public void setContractStatus(Integer contractStatus) {
        super.setContractStatus(contractStatus);
    }
    /**
     * 合同状态
     */
//    private Integer contractStatus;

    private Integer templateId;

    /**
     * 续租日期
     */
    private LocalDate reletDate;
    /**
     * 续租说明
     */
    private String reletRemark;
    /**
     * 续租附件
     */
    private String reletPath;

    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Long reletContractId;


    /**
     * 审核状态 1审核中,待审核 2 审核通过 3.未通过
     */
//    private Integer auditStatus;

    public static final String TABLE_NAME = "tb_contract_lease";


    public static final String ID = "id";

    public static final String CONTRACT_NO = "contract_no";

    public static final String NAME = "name";

    public static final String BEGIN_DATE = "begin_date";

    public static final String END_DATE = "end_date";

    public static final String CANTRACT_DATE = "cantract_date";

    public static final String FIRST_DATE = "first_date";

    public static final String FOLLOW_UP = "follow_up";

    public static final String PURPOSE = "purpose";

    public static final String TYPE = "type";

    public static final String PROPERTY = "property";

    public static final String LABEL = "label";

    public static final String REMARK = "remark";

    public static final String CLIENT = "client";

    public static final String FORM = "form";

    public static final String MAIN_BODY = "main_body";

    public static final String PATH = "path";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String CREATE_USER = "create_user";

    public static final String UPDATE_USER = "update_user";

    public static final String STATUS = "status";

    public static final String DELETED = "deleted";

    public static final String CONTRACT_STATUS = "contract_status";

    public static final String AUDIT_STATUS = "audit_status";

    @Override
    public Serializable pkVal() {
        return this.getId();
    }

}
