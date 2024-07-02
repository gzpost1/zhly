package cn.cuiot.dmp.lease.dto.contract;

import cn.cuiot.dmp.lease.entity.TbContractCancelEntity;
import cn.cuiot.dmp.lease.entity.TbContractIntentionEntity;
import cn.cuiot.dmp.lease.entity.TbContractLeaseBackEntity;
import cn.cuiot.dmp.lease.entity.TbContractLeaseEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description 提交, 签约, 退定, 作废,退租  按钮 统一入参
 * @Date 2024/06/17 16:40
 * @Created by Mujun
 */
@Data
public class ContractParam implements Serializable {
    /**
     * 合同id
     */
    @NotNull(message = "合同id不能为空")
    private Long id;

    /**
     * 退订和作废 需要传入
     */
    private TbContractCancelEntity contractCancelEntity;

    /**
     * 意向合同信息 签约需要传入
     */
    private Long leaseId;

    /**
     * 租赁合同 退租需要传入
     */
    private TbContractLeaseBackEntity contractLeaseBackEntity;






}
