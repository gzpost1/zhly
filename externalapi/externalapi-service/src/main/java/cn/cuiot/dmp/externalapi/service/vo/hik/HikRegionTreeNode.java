package cn.cuiot.dmp.externalapi.service.vo.hik;

import cn.cuiot.dmp.common.bean.TreeNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 海康区域树
 * @author: wuyongchong
 * @date: 2024/10/17 16:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HikRegionTreeNode extends TreeNode<HikRegionTreeNode> {

    private String indexCode;

    private String parentIndexCode;

    private String name;

    private String regionPath;

    private String regionPathName;

    public HikRegionTreeNode() {
    }

    public HikRegionTreeNode(String id, String parentId, String title) {
        super(id, parentId, title);
    }

}
