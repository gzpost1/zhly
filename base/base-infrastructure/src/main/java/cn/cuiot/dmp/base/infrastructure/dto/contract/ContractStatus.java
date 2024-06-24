package cn.cuiot.dmp.base.infrastructure.dto.contract;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

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
public class ContractStatus implements Serializable {
    private LocalDate beginDate;
    private LocalDate endDate;
    private Integer contractStatus;
    /**
     * 1.意向合同 3.租赁合同
     */
    private Integer type;

    private Long houseId;

}
