package cn.cuiot.dmp.system.application.param.vo;

import cn.cuiot.dmp.common.bean.TreeNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author caorui
 * @date 2024/5/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SystemOptionTypeTreeNodeVO extends TreeNode<SystemOptionTypeTreeNodeVO> {

    private static final long serialVersionUID = -2105149593272419028L;

    /**
     * 系统选项名称
     */
    private String name;

    /**
     * 系统选项类型
     */
    private Byte systemOptionType;

    /**
     * 层级类型
     */
    private Byte levelType;

    public SystemOptionTypeTreeNodeVO(String id, Byte systemOptionType, String parentId,
                                      String name, Byte levelType) {
        super(id, parentId, name);
        this.name = name;
        this.levelType = levelType;
        this.systemOptionType = systemOptionType;
    }

}
