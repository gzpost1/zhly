package cn.cuiot.dmp.system.domain.aggregate;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/4/26
 */
@Data
public class FormConfig implements Serializable {

    private static final long serialVersionUID = -340520698436360336L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 表单名称
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
     * 分类名称（合并后的层级名称，e.g.巡检>设备巡检）
     */
    private String typeName;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

    /**
     * 表单配置详情
     */
    private String formConfigDetail;

}
