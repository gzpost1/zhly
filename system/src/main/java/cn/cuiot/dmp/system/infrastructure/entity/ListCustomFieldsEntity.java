package cn.cuiot.dmp.system.infrastructure.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import cn.cuiot.dmp.base.infrastructure.persistence.handler.JsonTypeHandler;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashSet;

/**
 * 列表自定义字段
 *
 * @Author: zc
 * @Date: 2024-09-03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "tb_list_custom_fields", autoResultMap = true)
public class ListCustomFieldsEntity extends YjBaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 企业ID
     */
    @TableField(value = "company_id")
    private Long companyId;

    /**
     * 列表接口定义标识
     */
    @TableField(value = "identification")
    private String identification;

    /**
     * 展示字段json列表
     */
    @TableField(value = "`fields`", typeHandler = JsonTypeHandler.class)
    private LinkedHashSet<Object> fields;

    private static final long serialVersionUID = 1L;
}