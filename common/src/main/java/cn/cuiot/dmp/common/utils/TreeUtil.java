package cn.cuiot.dmp.common.utils;

import cn.cuiot.dmp.common.bean.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caorui
 * @date 2024/4/29
 */
public class TreeUtil {

    /**
     * 生成树
     */
    public static <T extends TreeNode<T>> List<T> makeTree(List<T> list) {
        List<T> tree = new ArrayList<T>();
        if (null != list) {
            TreeNode<T> parentNode = new TreeNode<>();
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
