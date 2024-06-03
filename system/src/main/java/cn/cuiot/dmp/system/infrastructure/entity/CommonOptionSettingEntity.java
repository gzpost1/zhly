package cn.cuiot.dmp.system.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/14
 */
@Data
@TableName(value = "common_option_setting", autoResultMap = true)
public class CommonOptionSettingEntity implements Serializable {

    private static final long serialVersionUID = -1145964792883152392L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 常用选项设置名称
     */
    private String name;

    /**
     * 常用选项ID
     */
    private Long commonOptionId;

    /**
     * 逻辑删除，1已删除，0未删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore
    private Integer deletedFlag;

    /**
     * 排序
     */
    private Byte sort;

}
