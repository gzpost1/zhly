package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * @author wqd
 * @classname FactoryParkParkListResDto
 * @description
 * @date 2023/1/13
 */
@Data
public class FactoryParkParkListResDto {

    private String parkId;

    private String parkCode;

    private String parkName;

    private String parkType;

    private String otherParkTypeName;

    private String createTime;

    private String deptName;

    private String areaName;

    private String address;

    private String description;

    private String deptTreePath;
}
