package cn.cuiot.dmp.base.infrastructure.dto.contract;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ContractStatusVo implements Serializable {
    //意向合同
    Map<Long, List<ContractStatus>> intentionMap;
    //租赁合同
    Map<Long, List<ContractStatus>> leaseMap;

}
