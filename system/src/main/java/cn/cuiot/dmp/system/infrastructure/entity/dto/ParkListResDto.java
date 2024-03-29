package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * @author wqd
 * @classname ParkListResDto
 * @description
 * @date 2023/1/11
 */
@Data
public class ParkListResDto extends ComplexListResDto {

    private String deptId;

    private String deptTreePath;

    private String parkId;

    private String parkCode;

    private String parkName;

    private String createTime;

    private String deptName;

    private String areaName;

    private String areaCode;

    private String address;

    private String description;

    private String parkType;

    private String otherParkTypeName;

    private String longitude;

    private String latitude;

}
