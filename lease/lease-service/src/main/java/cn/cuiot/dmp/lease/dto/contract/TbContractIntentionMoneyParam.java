package cn.cuiot.dmp.lease.dto.contract;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.*;
import cn.cuiot.dmp.common.bean.PageQuery;;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * 意向金
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
public class TbContractIntentionMoneyParam extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 收费项目
     */
    private String project;

    /**
     * 意向标的
     */
    private String bid;

    /**
     * 应收金额
     */
    private BigDecimal amount;

    /**
     * 备注
     */
    private String remark;

    private Long contractId;


}
