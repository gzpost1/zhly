package cn.cuiot.dmp.lease.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 收费管理-催款计划vo
 *
 * @author zc
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChargeCollectionPlanPageVo extends ChargeCollectionPlanVo {

    /**
     * 楼盘列表名称
     */
    private String buildingsName;

    /**
     * 收费项目名称
     */
    private String chargeItemsName;

    /**
     * 操作人
     */
    private String operatorName;

    /**
     * 创建人id
     */
    private Long createUser;
}