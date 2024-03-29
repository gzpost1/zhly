package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * @author wqd
 * @classname RegionListResDto
 * @description
 * @date 2023/1/12
 */
@Data
public class RegionListResDto {

    private String regionId;

    private String deptTreePath;

    private String parkId;

    private String deptId;

    private String parkName;

    private String regionName;

    private String regionType;

    private String otherRegionTypeName;

    private String regionCode;

    private String description;

    private String parkType;

    private String createTime;
}
