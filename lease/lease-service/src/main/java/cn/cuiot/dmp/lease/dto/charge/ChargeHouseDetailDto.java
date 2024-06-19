package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

/**
 * @Description 获取房屋欠费等相关信息
 * @Date 2024/6/12 20:17
 * @Created by libo
 */
@Data
public class ChargeHouseDetailDto {

    /**
     * 房屋主键
     */
    private Long houseId;

    /**
     * 房屋名称
     */
    private String houseName;

    /**
     * 房屋编号
     */
    private String houseCode;

    /**
     * 业主名称
     */
    private String ownerName;

    /**
     * 业主联系电话
     */
    private String ownerPhone;

    /**
     * ■总欠费金额
     */
    private Integer totalArrears = 0;

    /**
     * ■已缴费用
     */
    private Integer paidAmount = 0;

    /**
     * ■可退押金：
     */
    private Integer depositRefundable = 0;
}
