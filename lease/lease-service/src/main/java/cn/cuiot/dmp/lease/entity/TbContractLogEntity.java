package cn.cuiot.dmp.lease.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 合同操作日志
 *
 * @author MJ~
 * @since 2024-06-13
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_contract_log")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TbContractLogEntity extends Model<TbContractLogEntity> {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 操作项
     */
    private String operation;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作时间
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operTime;

    /**
     * 操作详情
     */
    private String operDesc;

    /**
     * 其他功能-附件
     */
    private String path;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 相关id
     */
    private String extId;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    public static final String TABLE_NAME = "tb_contract_log";


    public static final String ID = "id";

    public static final String OPERATION = "operation";

    public static final String OPERATOR = "operator";

    public static final String OPER_TIME = "oper_time";

    public static final String OPER_DESC = "oper_desc";

    public static final String PATH = "path";

    public static final String CREATE_USER = "create_user";

    public static final String CREATE_TIME = "create_time";

    @Override
    public Serializable pkVal() {
        return null;
    }

}
