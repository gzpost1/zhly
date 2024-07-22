package cn.cuiot.dmp.lease.dto.price;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/6/24
 */
@Data
public class PriceManageDetailCreateDTO implements Serializable {

    private static final long serialVersionUID = 5584368854504054777L;

    /**
     * 房屋编码
     */
    @NotNull(message = "房屋编码不能为空")
    private Long houseId;

    /**
     * 房号
     */
    @NotBlank(message = "房号不能为空")
    private String houseCode;

    /**
     * 建筑面积（支持4位小数，最长可输入15位）
     */
    private Double buildingArea;

    /**
     * 收费面积（支持4位小数，最长可输入15位）
     */
    private Double chargeArea;

    /**
     * 定价
     */
    @NotNull(message = "定价不能为空")
    private Integer price;

    /**
     * 底价单价
     */
    private Integer priceBase;

    /**
     * 成本单价
     */
    private Integer priceCost;

    /**
     * 备注
     */
    private String remark;

}
