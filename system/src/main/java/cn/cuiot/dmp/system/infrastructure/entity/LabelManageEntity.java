package cn.cuiot.dmp.system.infrastructure.entity;

import cn.cuiot.dmp.base.infrastructure.persistence.handler.JsonTypeHandler;
import cn.cuiot.dmp.base.infrastructure.persistence.mapper.BaseEntity;
import cn.cuiot.dmp.common.enums.LabelManageTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 系统配置-标签管理表
 *
 * @author caorui
 * @date 2024/6/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "label_manage", autoResultMap = true)
public class LabelManageEntity extends BaseEntity {

    private static final long serialVersionUID = 8923089430083346060L;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 标签管理类型
     * @see LabelManageTypeEnum
     */
    private Byte labelManageType;

    /**
     * 标签列表
     */
    @TableField(typeHandler = JsonTypeHandler.class)
    private List<String> labelList;

}
