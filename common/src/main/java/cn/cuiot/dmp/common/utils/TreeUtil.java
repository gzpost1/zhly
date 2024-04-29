package cn.cuiot.dmp.common.utils;

import cn.cuiot.dmp.common.bean.TreeNode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: wuyongchong
 * @date: 2024/4/29 14:36
 */
public class TreeUtil {

    /**
     * 生成树
     */
    public static <T extends TreeNode> List<T> makeTree(List<T> list) {
        List<T> tree = new ArrayList<T>();
        if (null != list) {
            TreeNode parentNode = new TreeNode();
            for (T node : list) {
                parentNode.setId(node.getParentId());
                int index = list.indexOf(parentNode);
                if (index != -1) {
                    (list.get(index)).addChild(node);
                } else {
                    tree.add(node);
                }
            }
        }
        return tree;
    }

}