package cn.cuiot.dmp.common.bean;

import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author caorui
 * @date 2024/4/29
 */
@Data
public class TreeNode<T extends TreeNode<T>> implements Serializable {

    private static final long serialVersionUID = -5672953422801724945L;

    private String id;
    private String parentId;
    private String title;
    private String label;
    //private boolean selected=false;
    //private boolean checked=false;
    //private boolean expand=false;
    private List<T> children = new ArrayList<>();

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
