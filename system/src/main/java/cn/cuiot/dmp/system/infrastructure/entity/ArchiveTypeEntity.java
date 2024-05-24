package cn.cuiot.dmp.system.infrastructure.entity;

import cn.cuiot.dmp.common.enums.ArchiveTypeEnum;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/19
 */
@Data
@TableName(value = "archive_type", autoResultMap = true)
public class ArchiveTypeEntity implements Serializable {

    private static final long serialVersionUID = 1268785109881572935L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 档案类型名称
     */
    private String name;

    /**
     * 档案类型
     * @see ArchiveTypeEnum
     */
    private Byte archiveType;

    /**
     * 逻辑删除，1已删除，0未删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore
    private Integer deletedFlag;

}
