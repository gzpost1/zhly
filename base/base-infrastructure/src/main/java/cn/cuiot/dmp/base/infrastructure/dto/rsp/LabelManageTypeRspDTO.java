package cn.cuiot.dmp.base.infrastructure.dto.rsp;

import cn.cuiot.dmp.common.enums.LabelManageTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/6/19
 */
@Data
public class LabelManageTypeRspDTO implements Serializable {

    private static final long serialVersionUID = 8202319202644293923L;

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
    private List<LabelManageRspDTO> labelManageList;

}
