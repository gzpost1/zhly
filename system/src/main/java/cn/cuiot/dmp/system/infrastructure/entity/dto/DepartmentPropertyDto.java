package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * @author wen
 * @Date 2021/12/27
 */
@Data
public class DepartmentPropertyDto {
    /**
     * 自增ID
     */
    private Long id;
    /**
     * 组织id
     */
    private Long deptId;
    /**
     * 属性key
     */
    private String key;
    /**
     * 属性值
     */
    private String val;

}
