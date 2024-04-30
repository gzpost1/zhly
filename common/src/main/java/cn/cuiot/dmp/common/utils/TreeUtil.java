package cn.cuiot.dmp.common.utils;

import cn.cuiot.dmp.common.bean.TreeNode;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/4/29
 */
public class TreeUtil<T extends TreeNode<T>> {

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

    /**
     * 获取树状结构，过滤未匹配的树节点
     *
     * @param treeList 搜索的树列表
     * @param hitIds   命中的ids
     */
    public List<T> searchNode(List<T> treeList, List<String> hitIds) {
        List<T> filterTree = Lists.newArrayList();
        for (T t : treeList) {
            T node = filterTree(t, hitIds);
            filterTree.add(node);
        }
        return filterTree.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 过滤树
     *
     * @param tree 当前树节点
     * @param hitIds 命中的ids
     */
    private T filterTree(T tree, List<String> hitIds) {
        if (isRemoveNode(tree, hitIds)) {
            return null;
        }
        Iterator<T> iterator = tree.getChildren().iterator();
        while (iterator.hasNext()) {
            T child = iterator.next();
            deleteNode(child, iterator, hitIds);
        }
        return tree;
    }

    /**
     * 删除节点
     *
     * @param child 子节点
     * @param iterator 迭代器
     */
    private void deleteNode(T child, Iterator<T> iterator, List<String> hitIds) {
        if (isRemoveNode(child, hitIds)) {
            iterator.remove();
            return;
        }
        List<T> childrenList = child.getChildren();
        if (CollectionUtils.isEmpty(childrenList)) {
            return;
        }
        Iterator<T> children = childrenList.iterator();
        while (children.hasNext()) {
            T childChild = children.next();
            deleteNode(childChild, children, hitIds);
        }
    }

    /**
     * 判断该节点是否该删除
     *
     * @param root 根节点
     * @param hitIds 命中的节点
     * @return ture 需要删除  false 不能被删除
     */
    private boolean isRemoveNode(T root, List<String> hitIds) {
        List<T> children = root.getChildren();
        // 叶子节点
        if (CollectionUtils.isEmpty(children)) {
            return !hitIds.contains(root.getId());
        }
        // 子节点
        if (hitIds.contains(root.getId())) {
            return false;
        }
        boolean bool = true;
        for (T child : children) {
            if (!isRemoveNode(child, hitIds)) {
                bool = false;
                break;
            }
        }
        return bool;
    }

}
