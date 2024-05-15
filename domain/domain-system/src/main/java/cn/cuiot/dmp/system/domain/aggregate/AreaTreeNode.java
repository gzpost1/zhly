package cn.cuiot.dmp.system.domain.aggregate;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/14
 */
@Data
public class AreaTreeNode implements Serializable {

    private static final long serialVersionUID = -8443506170642052391L;

    /**
     * 区域编码，主键
     */
    private String id;

    /**
     * 上级/父级区域编码
     */
    private String parentId;

    /**
     * 区域名称
     */
    private String title;

}
