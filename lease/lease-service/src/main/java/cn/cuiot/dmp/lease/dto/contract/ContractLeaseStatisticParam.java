package cn.cuiot.dmp.lease.dto.contract;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

;

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
public class ContractLeaseStatisticParam implements Serializable {

    private static final long serialVersionUID = -1911523048940542904L;


    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 楼盘Id
     */
    private List<Long> loupanIds;

    /**
     * 部门ID
     */
    private List<Long> departmentIdList;

    private List<Integer> contractStatus;


}
