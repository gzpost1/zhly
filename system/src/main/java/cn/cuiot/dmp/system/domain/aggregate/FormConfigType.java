package cn.cuiot.dmp.system.domain.aggregate;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/4/26
 */
@Data
public class FormConfigType implements Serializable {

    private static final long serialVersionUID = -4754274194064189277L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 上级ID
     */
    private Long parentId;

    /**
     * 层级类型(0:根节点，默认叫全部；最多可添加4级；)
     */
    private Byte levelType;

    /**
     * 企业ID
     */
    private Long companyId;

}
