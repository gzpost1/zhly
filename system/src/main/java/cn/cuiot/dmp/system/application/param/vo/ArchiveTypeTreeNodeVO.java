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
public class ArchiveTypeTreeNodeVO extends TreeNode<ArchiveTypeTreeNodeVO> {

    private static final long serialVersionUID = -2105149593272419028L;

    /**
     * 档案名称
     */
    private String name;

    /**
     * 档案类型
     */
    private Byte archiveType;

    /**
     * 层级类型
     */
    private Byte levelType;

    public ArchiveTypeTreeNodeVO(String id, Byte archiveType, String parentId,
                                 String name, Byte levelType) {
        super(id, parentId, name);
        this.name = name;
        this.levelType = levelType;
        this.archiveType = archiveType;
    }

}
