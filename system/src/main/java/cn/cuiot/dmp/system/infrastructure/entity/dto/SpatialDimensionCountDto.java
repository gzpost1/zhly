package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * @author zhh
 * @description SpatialDimensionCountDto
 * @author: 空间树查询入参
 * @create: 2020-09-22 10:40
 */
@Data
public class SpatialDimensionCountDto {
    private String parentPath;

    private Long count;
}
