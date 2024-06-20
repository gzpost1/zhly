package cn.cuiot.dmp.system.application.param.dto;

import cn.cuiot.dmp.common.enums.LabelManageTypeEnum;
import cn.cuiot.dmp.system.infrastructure.entity.LabelManageEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/6/19
 */
@Data
public class LabelManageTypeDTO implements Serializable {

    private static final long serialVersionUID = 2857428138822438669L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 标签管理分类
     */
    private String name;

    /**
     * 标签管理类型
     * @see LabelManageTypeEnum
     */
    private Byte labelManageType;

    /**
     * 标签管理详情
     */
    private LabelManageEntity labelManage;

}
