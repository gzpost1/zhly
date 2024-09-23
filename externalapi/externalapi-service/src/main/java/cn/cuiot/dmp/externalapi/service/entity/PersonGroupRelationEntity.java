package cn.cuiot.dmp.externalapi.service.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 人员分组关联表
 *
 * @Author: zc
 * @Date: 2024-09-05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "tb_person_group_relation")
public class PersonGroupRelationEntity extends YjBaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 业务类型（1:宇泛门禁；2:格物门禁）
     */
    @TableField(value = "business_type")
    private Byte businessType;

    /**
     * 数据id
     */
    @TableField(value = "data_id")
    private Long dataId;

    /**
     * 人员分组id
     */
    @TableField(value = "person_group_id")
    private Long personGroupId;

    private static final long serialVersionUID = 1L;
}