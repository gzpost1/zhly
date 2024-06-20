package cn.cuiot.dmp.lease.entity;

import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.model.HousesArchivesVo;
import cn.cuiot.dmp.common.constant.EntityConstants;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
import java.util.List;

/**
 * 意向合同
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
@TableName(value = "tb_contract_intention",autoResultMap = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TbContractIntentionEntity extends Model<TbContractIntentionEntity> {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 租赁合同id
     */
    private Long contractLeaseId;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 合同名称
     */
    @NotNull(message = "合同名称不能为空")
    private String name;

    @TableField(exist = false)
    private List<Long> queryIds;

    /**
     * 签订日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate cantractDate;
    @TableField(exist = false)
    private LocalDate cantractBeginDate;
    @TableField(exist = false)
    private LocalDate cantractEndDate;

    /**
     * 合同开始日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "合同开始日期不能为空")
    private LocalDate beginDate;

    /**
     * 查询开始日期 开始日期
     */
    @TableField(exist = false)
    private LocalDate beginDateBegin;
    /**
     * 查询开始日期 结束日期
     */
    @TableField(exist = false)
    private LocalDate beginDateEnd;
    /**
     * 合同结束日期 开始日期
     */
    @TableField(exist = false)
    private LocalDate endDateBegin;
    /**
     * 合同结束日期 结束日期
     */
    @TableField(exist = false)
    private LocalDate endDateEnd;
    /**
     * 意向标(房屋名称)
     */
    @TableField(exist = false)
    private String houseName;




    /**
     * 合同结束日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "合同结束日期不能为空")
    private LocalDate endDate;

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

    /**
     * 审核状态
     * 1:审核中待审核 2:审核通过 3:未通过
     */
    private Integer auditStatus;

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

    /**
     * 意向房屋
     */
    @TableField(exist = false)
    private List<HousesArchivesVo> houseList;
    /**
     * 意向金
     */
    @TableField(exist = false)
    private List<TbContractIntentionMoneyEntity> moneyList;
    /**
     * 退订信息
     */
    @TableField(exist = false)
    private TbContractCancelEntity cancelInfo;
    /**
     * 作废信息
     */
    @TableField(exist = false)
    private TbContractCancelEntity uselessInfo;

    /**
     * 合同状态
     */
    private Integer contractStatus;

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
        return null;
    }

}
