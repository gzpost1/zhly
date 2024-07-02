package cn.cuiot.dmp.lease.dto.contract;

import cn.cuiot.dmp.lease.entity.TbContractCancelEntity;
import cn.cuiot.dmp.lease.entity.TbContractLeaseBackEntity;
import cn.cuiot.dmp.lease.entity.TbContractLeaseEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @Description 续租
 * @Date 2024/06/17 16:40
 * @Created by Mujun
 */
@Data
public class ContractReletParam implements Serializable {
    /**
     * 合同id
     */
    @NotNull(message = "合同id不能为空")
    private Long id;

    /**
     * 续租合同
     */
    private TbContractLeaseEntity contractLeaseReletEntity;






}
