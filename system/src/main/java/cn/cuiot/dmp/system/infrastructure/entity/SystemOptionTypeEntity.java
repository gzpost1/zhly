package cn.cuiot.dmp.system.infrastructure.entity;

import cn.cuiot.dmp.common.enums.SystemOptionTypeEnum;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统配置-常用选项-系统选项
 *
 * @author caorui
 * @date 2024/5/19
 */
@Data
@TableName(value = "system_option_type", autoResultMap = true)
public class SystemOptionTypeEntity implements Serializable {

    private static final long serialVersionUID = 1268785109881572935L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 系统选项类型名称
     */
    private String name;

    /**
     * 系统选项类型
     * @see SystemOptionTypeEnum
     */
    private Byte systemOptionType;

    /**
     * 逻辑删除，1已删除，0未删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore
    private Integer deletedFlag;

}
