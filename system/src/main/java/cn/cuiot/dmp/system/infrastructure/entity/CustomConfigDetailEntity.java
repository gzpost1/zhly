package cn.cuiot.dmp.system.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/16
 */
@Data
@TableName(value = "custom_config_detail", autoResultMap = true)
public class CustomConfigDetailEntity implements Serializable {

    private static final long serialVersionUID = 4997629132409373243L;

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
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore
    private Integer deletedFlag;

}
