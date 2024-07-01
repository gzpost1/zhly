package cn.cuiot.dmp.system.infrastructure.entity;

import cn.cuiot.dmp.base.infrastructure.persistence.mapper.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 表单配置-常用选项分类表
 *
 * @author caorui
 * @date 2024/4/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "common_option_type", autoResultMap = true)
public class CommonOptionTypeEntity extends BaseEntity {

    private static final long serialVersionUID = -3364124107350251155L;

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
