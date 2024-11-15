package cn.cuiot.dmp.base.infrastructure.dto.companyinit;

import lombok.Data;

import java.io.Serializable;

/**
 * 表单配置-常用选项分类表
 *
 * @Author: zc
 * @Date: 2024-11-14
 */
@Data
public class CommonOptionTypeSyncDTO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 选项类别（0：自定义，1：交易方式，2：收费方式）
     */
    private Byte category;

    /**
     * 上级ID
     */
    private Long parentId;

    /**
     * 层级类型(0:根节点，默认叫全部；最多可添加4级；)
     */
    private Byte levelType;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 名称路径（e.g.巡检>设备巡检）
     */
    private String pathName;

}
