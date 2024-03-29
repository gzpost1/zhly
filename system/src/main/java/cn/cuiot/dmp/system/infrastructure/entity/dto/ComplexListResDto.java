package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * @author wqd
 * @classname ComplexListResDto
 * @description
 * @date 2023/2/20
 */
@Data
public class ComplexListResDto {

    private String deptId;

    private String deptTreePath;

    private String complexId;

    private String complexCode;

    private String complexName;

    private String createTime;

    private String deptName;

    private String areaName;

    private String areaCode;

    private String address;

    private String description;

    private String longitude;

    private String latitude;

}
