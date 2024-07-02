package cn.cuiot.dmp.lease.dto.contract;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.*;
import cn.cuiot.dmp.common.bean.PageQuery;;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * 退租信息
 *
 * @author MJ~
 * @since 2024-06-26
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TbContractLeaseBackParam extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 合同id
     */
    private Long contractId;

    /**
     * 合同截止日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate endDate;

    /**
     * 退租类型
     */
    private String type;

    /**
     * 退租原因
     */
    private String reason;

    /**
     * 物业验收交接日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate handoverDate;

    /**
     * 退租说明
     */
    private String remark;

    /**
     * 退租协议附件
     */
    private String path;


}
