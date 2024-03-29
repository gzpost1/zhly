package cn.cuiot.dmp.system.infrastructure.entity.dto;


import lombok.Data;

/**
 * @author lx
 * UserLabel信息实体
 */
@Data
public class LabelTypeDto {

    /**
     * 标签id
     */
    private Integer id;

    /**
     * 标签名称
     */
    private String labelName;

    /**
     * 标签类型（0：用户标签；1：账户标签）
     */
    private String labelType;

}
