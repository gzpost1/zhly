package cn.cuiot.dmp.lease.dto.contract;

import cn.cuiot.dmp.common.bean.PageQuery;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TbContractChargeParam extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 合同编号
     */
    private String contractId;

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

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;


}
