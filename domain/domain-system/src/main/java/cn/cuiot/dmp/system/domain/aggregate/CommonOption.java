package cn.cuiot.dmp.system.domain.aggregate;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/4/26
 */
@Data
public class CommonOption implements Serializable {

    private static final long serialVersionUID = -581125814590108701L;

    /**
     * 主键ID
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
     * 分类名称（合并后的层级名称，e.g.巡检>设备巡检）
     */
    private String typeName;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

    /**
     * 常用选项设置
     */
    private List<CommonOptionSetting> commonOptionSettings;

}
