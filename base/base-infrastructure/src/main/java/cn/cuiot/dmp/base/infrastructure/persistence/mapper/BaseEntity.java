package cn.cuiot.dmp.base.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description Entity基类，封装了增删改的信息字段
 * @Author 犬豪
 * @Date 2023/9/21 10:01
 * @Version V1.0
 */
@Getter
@Setter
public abstract class BaseEntity implements Serializable {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 逻辑删除，1已删除，0未删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore
    private Integer deletedFlag;

    /**
     * 删除人id
     */
    private String deletedBy;

    /**
     * 删除时间
     */
    private LocalDateTime deletedOn;

    /**
     * 删除者类型（1：system、2：用户、3：open api）
     */
    private Integer deleteByType;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    /**
     * 创建者。取值：SYSTEM：系统生成的；{userKey}：Portal用户创建的；{appKey}：API创建的。
     */
    @TableField(fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 创建者类型（1：system、2：用户、3：open api）
     */
    private Integer createdByType;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedOn;

    /**
     * 更新者。取值：{userKey}：Portal用户更新的；{appKey}：API更新的。
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    /**
     * 更新者类型（1：system、2：用户、3：open api）
     */
    private Integer updatedByType;
}
