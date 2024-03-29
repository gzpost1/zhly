package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * @author wensq
 * @version 1.0
 * @date 2022/9/18 10:32
 * 行政区域查询出参
 */
@Data
public class AreaDto {

    /**
     * 小区编码
     */
    private String code;

    /**
     * 小区名
     */
    private String name;

    /**
     * 等级
     */
    private Integer level;
}
