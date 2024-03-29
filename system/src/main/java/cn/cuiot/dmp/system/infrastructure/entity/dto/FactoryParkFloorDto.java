package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * @author hk
 * @version 1.0
 * @description: 厂园区楼层查询返回出参
 * @date 2023/1/12 11:06
 */
@Data
public class FactoryParkFloorDto extends DepartmentDto {

    /**
     * 园区名称
     */
    private String parkName;
    /**
     * 园区id
     */
    private String parkId;

    /**
     * 区域名称
     */
    private String regionName;

    /**
     * 区域Id
     */
    private String regionId;

    /**
     * 楼座名称
     */
    private String buildingName;

    /**
     * 楼座id
     */
    private String buildingId;

    /**
     * 楼座层数
     */
    private String buildingNum;

    /**
     * 楼层
     */
    private String currentFloor;

    /**
     * 楼层id
     */
    private String floorId;

    /**
     * 楼层名称
     */
    private String floorName;

    /**
     * 备注
     */
    private String description;
}
