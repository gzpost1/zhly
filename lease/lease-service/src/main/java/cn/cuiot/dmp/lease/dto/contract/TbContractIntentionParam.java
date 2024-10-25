package cn.cuiot.dmp.lease.dto.contract;

import cn.cuiot.dmp.common.bean.PageQuery;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TbContractIntentionParam extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private List<Long> queryIds;
    /**
     * 租赁合同id
     */
    private Long contractLeaseId;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 意向标(房屋名称)
     */
    private String houseName;

    /**
     * 合同名称
     */
    private String name;
    /**
     * 标签 ","分割
     */
    private String label;

    /**
     * 签订日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate cantractDate;

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

    private LocalDate cantractBeginDate;

    private LocalDate cantractEndDate;
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
     * 签订客户
     */
    private String client;

    /**
     * 意向备注
     */
    private String remark;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

    private Byte status;

    @TableLogic
    @Builder.Default
    private Byte deleted = 0;

    /**
     * 合同状态
     */
    private Integer contractStatus;
    /**
     * 审核状态
     */
    private Integer auditStatus;


    /**
     * 续租日期
     */
    private LocalDate reletDate;
    /**
     * 续租说明
     */
    private LocalDate reletRemark;
    /**
     * 续租附件
     */
    private LocalDate reletPath;

    private Integer templateId;

    /**
     * 所属组织
     */
    private Long orgId;
    private List<Long> departmentIdList;

}
