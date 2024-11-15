package cn.cuiot.dmp.base.infrastructure.dto.companyinit;

import lombok.Data;

import java.io.Serializable;

/**
 * 表单配置-常用选项表
 *
 * @Author: zc
 * @Date: 2024-11-13
 */
@Data
public class CommonOptionSyncDTO implements Serializable {

    /**
     * ido
     */
    private Long id;

    /**
     * 常用选项名称
     */
    private String name;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 分类ID
     */
    private Long typeId;

    /**
     * 选项类别（0：自定义，1：交易方式，2：收费方式）
     */
    private Byte typeCategory;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;
}
