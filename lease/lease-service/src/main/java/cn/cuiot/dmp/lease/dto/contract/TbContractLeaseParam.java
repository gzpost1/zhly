package cn.cuiot.dmp.lease.dto.contract;

import cn.cuiot.dmp.base.infrastructure.model.HousesArchivesVo;
import cn.cuiot.dmp.lease.entity.TbContractChargeEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import lombok.*;
import cn.cuiot.dmp.common.bean.PageQuery;;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * 租赁合同
 *
 * @author MJ~
 * @since 2024-06-19
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TbContractLeaseParam extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private List<Long> queryIds;


    private List<Long> queryNotInIds;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 合同名称
     */
    private String name;

    /**
     * 合同开始日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate beginDate;

    /**
     * 合同结束日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate endDate;

    /**
     * 签订日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate cantractDate;

    /**
     * 合同表单值
     */
    private String formData;

    /**
     * 首期应收日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate firstDate;

    private LocalDate cantractBeginDate;

    private LocalDate cantractEndDate;
    /**
     * 退租查询起始日期
     */
    private LocalDate leaseBackBeginDate;

    /**
     * 退租查询终止日期
     */
    private LocalDate leaseBackEndDate;
    /**
     * 查询开始日期 开始日期
     */
    private LocalDate beginDateBegin;
    /**
     * 查询开始日期 结束日期
     */
    private LocalDate beginDateEnd;
    /**
     * 合同结束日期 开始日期
     */
    private LocalDate endDateBegin;
    /**
     * 合同结束日期 结束日期
     */
    private LocalDate endDateEnd;

    /**
     * 跟进人

     */
    private String followUp;
    private String followUpName;


    /**
     * 租赁用途
     */
    private String purpose;

    /**
     * 合同类型
     */
    private String type;

    /**
     * 合同性质
     */
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
     * 合同主体
     */
    private String mainBody;

    /**
     * 其他功能-附件
     */
    private String path;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

    private Byte status;

    /**
     * 租赁房屋
     */
    private List<HousesArchivesVo> houseList;

    /**
     * 费用条款
     */
    private List<TbContractChargeEntity> chargeList;

    @TableLogic
    @Builder.Default
    private Byte deleted = 0;

    /**
     * 合同状态
     */
    private Integer contractStatus;

    /**
     * 审核状态 1审核中,待审核 2 审核通过 3.未通过
     */
    private Integer auditStatus;

    /**
     * 意向标(房屋名称)
     */
    private String houseName;


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

    private Long templateId;

    private Long reletContractId;

    private Boolean usefullLease ;

    /**
     * 所属组织
     */
    private Long orgId;

}
