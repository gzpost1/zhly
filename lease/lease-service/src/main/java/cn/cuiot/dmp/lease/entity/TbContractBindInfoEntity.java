package cn.cuiot.dmp.lease.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 意向合同关联信息
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
@TableName("tb_contract_bind_info")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TbContractBindInfoEntity extends Model<TbContractBindInfoEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 意向合同id
     */
    private Long intentionId;

    /**
     * 关联id
     */
    private Long bindId;

    /**
     * 1.房屋id 2.意向金id
     */
    private Integer type;

    public static final String TABLE_NAME = "tb_contract_bind_info";


    public static final String INTENTION_ID = "intention_id";

    public static final String BIND_ID = "bindId";

    public static final String TYPE = "type";

    @Override
    public Serializable pkVal() {
        return null;
    }

}
