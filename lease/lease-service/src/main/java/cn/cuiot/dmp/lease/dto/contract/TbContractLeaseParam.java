package cn.cuiot.dmp.lease.dto.contract;

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
     * 首期应收日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate firstDate;

    /**
     * 跟进人
     */
    private String followUp;

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


}
