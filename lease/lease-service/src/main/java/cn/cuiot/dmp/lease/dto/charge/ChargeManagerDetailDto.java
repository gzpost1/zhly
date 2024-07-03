package cn.cuiot.dmp.lease.dto.charge;

import cn.cuiot.dmp.base.infrastructure.utils.MathTool;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description 收费管理-收银台-缴费管理详情
 * @Date 2024/6/12 20:29
 * @Created by libo
 */
@Data
public class ChargeManagerDetailDto extends ChargeManagerPageDto{
    /**
     * 本金不含税	应收金额-应收金额*税率=本金不含税
     */
    private Integer receivableAmountNotTax = 0;

    /**
     * 本金欠收	应收金额-本金实收=本金欠收
     */
    private Integer receivableAmountOwe = 0;

    /**
     * 违约金额不含税 单次：违约金-违约金*税率=违约金额不含税  计算累计的违约金不含税
     */
    private Integer liquidatedDamagesNotTax = 0;

    /**
     * 违约金应收
     */
    private Integer liquidatedDamagesNeed = 0;

    /**
     * 违约金实收
     */
    private Integer liquidatedDamagesReceived = 0;

    /**
     * 实收合计 本金实收+违约金实收=实收合计
     */
    private Integer totalReceived = 0;

    /**
     * 创建用户名称
     */
    private String createUserName;

    private Long createUser;

    public Integer getReceivableAmountNotTax() {
        return getReceivableAmount() - getReceivableAmountTax();
    }

    public Integer getTotalReceived() {
        return getReceivableAmountReceived() + getLiquidatedDamagesReceived();
    }


    public Integer getReceivableAmountOwe() {
        return getReceivableAmount() - getReceivableAmountReceived();
    }
}
