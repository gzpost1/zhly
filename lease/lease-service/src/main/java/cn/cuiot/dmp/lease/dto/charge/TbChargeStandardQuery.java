package cn.cuiot.dmp.lease.dto.charge;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

/**
 * @Description 收费标准查询
 * @Date 2024/10/10 10:56
 * @Created by libo
 */
@Data
public class TbChargeStandardQuery extends PageQuery {
    /**
     * 收费项目Id
     */
    private Long chargeProjectId;

    /**
     * 收费标准
     */
    private String chargeStandard;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 是否启用 0：禁用 1：启用
     */
    private Byte status;
}
