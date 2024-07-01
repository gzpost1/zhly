package cn.cuiot.dmp.lease.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.NotNull;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.base.application.enums.BeanValidationGroup;
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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_contract_lease_back")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TbContractLeaseBackEntity extends Model<TbContractLeaseBackEntity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "id不能为空",groups = BeanValidationGroup.Update.class)
    private Long id;

    /**
     * 合同id
     */
    @NotNull(message = "退租合同id不能为空")
    private Long contractId;

    /**
     * 合同截止日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "退租日期不能为空")
    private LocalDate endDate;

    /**
     * 退租类型
     */
    @NotNull(message = "退租类型不能为空")
    private String type;

    /**
     * 退租原因
     */
    @NotNull(message = "退租原因不能为空")
    private String reason;

    /**
     * 物业验收交接日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "物业验收交接日期不能为空")
    private LocalDate handoverDate;

    /**
     * 退租说明
     */
    private String remark;

    /**
     * 退租协议附件
     */
    private String path;

    public static final String TABLE_NAME = "tb_contract_lease_back";


    public static final String ID = "id";

    public static final String CONTRACT_ID = "contract_id";

    public static final String END_DATE = "end_date";

    public static final String TYPE = "type";

    public static final String REASON = "reason";

    public static final String HANDOVER_DATE = "handover_date";

    public static final String REMARK = "remark";

    public static final String PATH = "path";

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
