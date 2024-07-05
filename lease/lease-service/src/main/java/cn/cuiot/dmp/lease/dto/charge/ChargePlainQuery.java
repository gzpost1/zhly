package cn.cuiot.dmp.lease.dto.charge;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

/**
 * @Description 自动生成计划分页查询
 * @Date 2024/6/20 10:43
 * @Created by libo
 */
@Data
public class ChargePlainQuery extends PageQuery {
    /**
     * 收费项目id
     */
    private Long chargeItemId;

    /**
     * 状态 0停用 1启用
     */
    private Byte status;

    /**
     * 生成计划编码
     */
    private String plainCode;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 收费对象
     */
    private Long receivableObj;
}
