package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * @author wensq
 * @version 1.0
 * @description: 小区查询出参
 * @date 2022/9/14 18:06
 */
@Data
public class OrgTypeDto {

    /**
     * 账户类型id
     */
    private Integer id;

    /**
     * 账户类型名
     */
    private String name;
}
