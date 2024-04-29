package cn.cuiot.dmp.system.application.param.vo;

import cn.cuiot.dmp.common.bean.TreeNode;
import cn.cuiot.dmp.system.domain.aggregate.BusinessType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author caorui
 * @date 2024/4/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessTypeTreeNodeVO extends TreeNode<BusinessTypeTreeNodeVO> {

    private static final long serialVersionUID = -4940892942345661729L;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 层级类型(0:根节点，默认企业名称；最多可添加4级；)
     */
    private Byte levelType;

    /**
     * 企业ID
     */
    private Long companyId;

    public BusinessTypeTreeNodeVO(String id, String parentId, String name,
                                  Byte levelType, Long companyId) {
        super(id, parentId, name);
        this.name = name;
        this.levelType = levelType;
        this.companyId = companyId;
    }

}
