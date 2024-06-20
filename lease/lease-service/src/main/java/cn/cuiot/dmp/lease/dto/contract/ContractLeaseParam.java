package cn.cuiot.dmp.lease.dto.contract;

import cn.cuiot.dmp.lease.entity.TbContractCancelEntity;
import cn.cuiot.dmp.lease.entity.TbContractIntentionEntity;
import cn.cuiot.dmp.lease.entity.TbContractLeaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Date 2024/06/17 16:40
 * @Created by Mujun
 */
@Data
public class ContractLeaseParam implements Serializable {
    /**
     * 合同id
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
     * 租赁合同信息 签约需要传入
     */
    private TbContractLeaseEntity contractLeaseEntity;





}
