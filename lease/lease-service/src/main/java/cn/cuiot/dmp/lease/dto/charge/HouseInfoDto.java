package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

/**
 * @Description 房屋信息
 * @Date 2024/6/19 14:35
 * @Created by libo
 */
@Data
public class HouseInfoDto {
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
}
