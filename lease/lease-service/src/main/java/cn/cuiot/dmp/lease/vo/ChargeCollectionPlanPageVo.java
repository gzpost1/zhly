package cn.cuiot.dmp.lease.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "收费对象", orderNum = "3", width = 20)
    private String buildingsName;

    /**
     * 收费项目名称
     */
    @Excel(name = "收费项目", orderNum = "4", width = 20)
    private String chargeItemsName;

    /**
     * 操作人
     */
    @Excel(name = "创建人", orderNum = "7", width = 20)
    private String operatorName;

    /**
     * 创建人id
     */
    private Long createUser;
}