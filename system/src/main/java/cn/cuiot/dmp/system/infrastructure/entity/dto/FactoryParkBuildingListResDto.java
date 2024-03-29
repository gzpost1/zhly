package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * @author wqd
 * @classname FactoryParkBuildingListResDto
 * @description
 * @date 2023/1/13
 */
@Data
public class FactoryParkBuildingListResDto {

    private String buildingId;

    private String deptTreePath;

    private String regionId;

    private String parkId;

    private String deptId;

    private String parkName;

    private String regionName;

    private String buildingName;

    private String buildingFloor;

    private String description;
}
