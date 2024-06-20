package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

/**
 * @Description 实收管理查询
 * @Date 2024/6/13 15:41
 * @Created by libo
 */
@Data
public class PaidInManageMentQuery extends TbChargeManagerQuery{
    /**
     * 应收编码
     */
    private String receivableCode;

    /**
     * 实收编码
     */
    private String receivedCode;

    /**
     * 客户id
     */
    private Long customerUserId;
}
