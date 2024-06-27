package cn.cuiot.dmp.lease.entity;

import cn.cuiot.dmp.base.application.enums.BeanValidationGroup;
import cn.cuiot.dmp.base.application.enums.ContractEnum;
import cn.cuiot.dmp.base.infrastructure.model.HousesArchivesVo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * @Description TODO
 * @Date 2024-06-21 14:40
 * @Author by Mujun~
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
@AllArgsConstructor
public class BaseContractEntity extends Model {


    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "id不能为空",groups = BeanValidationGroup.Update.class)
    private Long id;

    @TableField(exist = false)
    private List<Long> queryIds;
    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 意向标(房屋名称)
     */
    @TableField(exist = false)
    private String houseName;
    /**
     * 审核状态
     * 1:审核中待审核 2:审核通过 3:未通过
     */
    private Integer auditStatus;


    public void setContractStatus(Integer contractStatus) {
        this.contractStatus = contractStatus;
    }

    /**
     * 合同状态
     */
    private Integer contractStatus;

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
     * 合同结束日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "合同结束日期不能为空")
    private LocalDate endDate;

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
     * 费用条款
     */
    @TableField(exist = false)
    private List<TbContractChargeEntity> chargeList;

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
     * 退租信息
     */
    @TableField(exist = false)
    private List<TbContractLeaseBackEntity> leaseBackList;

    @TableField(exist = false)
    private String createUserName;
    @TableField(exist = false)
    private String updateUserName;


}
