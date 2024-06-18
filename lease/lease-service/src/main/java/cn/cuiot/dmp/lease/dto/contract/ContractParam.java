package cn.cuiot.dmp.lease.dto.contract;

import cn.cuiot.dmp.lease.entity.TbContractCancelEntity;
import cn.cuiot.dmp.lease.entity.TbContractIntentionEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description 提交, 签约, 退定, 作废  按钮 统一入参
 * @Date 2024/06/17 16:40
 * @Created by Mujun
 */
@Data
public class ContractParam implements Serializable {
    /**
     * 意向合同id
     */
    private Long id;

    /**
     * 草稿和提交 需要传入
     */
    private TbContractIntentionEntity contractIntentionEntity;

    /**
     * 退订和作废 需要传入
     */
    private TbContractCancelEntity contractCancelEntity;



    /**
     * 租赁合同 ID
     */
    private Long contractLeaseId;


}
