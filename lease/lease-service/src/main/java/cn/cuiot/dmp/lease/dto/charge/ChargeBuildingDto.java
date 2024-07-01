package cn.cuiot.dmp.lease.dto.charge;

import lombok.Data;

/**
 * 收费管理-楼盘信息Dto
 *
 * @author zc
 */
@Data
public class ChargeBuildingDto {

    /**
     * 业务id
     */
    private Long dataId;

    /**
     * 楼盘名称
     */
    private String buildingName;
}