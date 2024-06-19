package cn.cuiot.dmp.system.infrastructure.entity;

import cn.cuiot.dmp.common.enums.LabelManageTypeEnum;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统配置-标签管理分类表
 *
 * @author caorui
 * @date 2024/6/19
 */
@Data
@TableName(value = "label_manage_type", autoResultMap = true)
public class LabelManageTypeEntity implements Serializable {

    private static final long serialVersionUID = -6854583448541792714L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标签管理分类名称
     */
    private String name;

    /**
     * 标签管理类型
     * @see LabelManageTypeEnum
     */
    private Byte labelManageType;

    /**
     * 逻辑删除，1已删除，0未删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore
    private Integer deletedFlag;

}
