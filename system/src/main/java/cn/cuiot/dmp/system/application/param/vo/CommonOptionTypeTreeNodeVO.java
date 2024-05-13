package cn.cuiot.dmp.system.application.param.vo;

import cn.cuiot.dmp.common.bean.TreeNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author caorui
 * @date 2024/5/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommonOptionTypeTreeNodeVO extends TreeNode<CommonOptionTypeTreeNodeVO> {

    private static final long serialVersionUID = -5803071977038002559L;

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

    public CommonOptionTypeTreeNodeVO(String id, String parentId, String name,
                                    Byte levelType, Long companyId) {
        super(id, parentId, name);
        this.name = name;
        this.levelType = levelType;
        this.companyId = companyId;
    }

}
