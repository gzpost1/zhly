package cn.cuiot.dmp.common.bean;

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.List;

/**
 * 树节点
 * @author: wuyongchong
 * @date: 2024/4/29 14:36
 */
public class TreeNode<T extends TreeNode> implements Serializable {

    private String id;
    private String parentId;
    private String title;
    private String label;
    private List<T> children = null;

    public TreeNode() {
    }

    public TreeNode(String id, String title) {
        this.id = id;
        this.title = title;
        this.label = title;
    }

    public TreeNode(String id, String parentId, String title) {
        this.id = id;
        this.parentId = parentId;
        this.title = title;
        this.label = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.label = title;
    }

    public String getLabel() {
        return this.title;
    }

    public void setLabel(String label) {
        this.label = label;
        this.title = title;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    public boolean hasChildren() {
        if (null != this.children && this.children.size() > 0) {
            return true;
        }
        return false;
    }

    public void addChild(T child) {
        if (this.children == null) {
            this.children = Lists.newArrayList();
        }
        this.children.add(child);
        child.setParentId(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj instanceof TreeNode)) {
            TreeNode target = (TreeNode) obj;
            return target.getId().equals(getId());
        }
        return false;
    }

}