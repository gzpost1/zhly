package cn.cuiot.dmp.lease.entity;

import cn.cuiot.dmp.base.infrastructure.persistence.mapper.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *租赁管理-定价管理明细
 *
 * @author caorui
 * @date 2024/6/21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "price_manage_detail", autoResultMap = true)
public class PriceManageDetailEntity extends BaseEntity {

    private static final long serialVersionUID = 5006255016575791725L;

    /**
     * 房屋编码
     */
    private Long houseId;

    /**
     * 房号
     */
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
