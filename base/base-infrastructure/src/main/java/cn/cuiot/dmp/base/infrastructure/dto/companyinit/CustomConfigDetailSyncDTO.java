package cn.cuiot.dmp.base.infrastructure.dto.companyinit;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zc
 * @Date: 2024-11-13
 */
@Data
public class CustomConfigDetailSyncDTO implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 自定义配置详情名称
     */
    private String name;

    /**
     * 自定义配置ID
     */
    private Long customConfigId;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

    /**
     * 逻辑删除，1已删除，0未删除
     */
    private Integer deletedFlag;

    /**
     * 排序
     */
    private Byte sort;

}
