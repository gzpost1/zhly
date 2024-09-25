package cn.cuiot.dmp.externalapi.service.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 人员分组
 *
 * @Author: zc
 * @Date: 2024-09-04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "tb_person_group")
public class PersonGroupEntity extends YjBaseEntity {
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
     * 分组名称
     */
    @TableField(value = "`name`")
    private String name;

    private static final long serialVersionUID = 1L;
}