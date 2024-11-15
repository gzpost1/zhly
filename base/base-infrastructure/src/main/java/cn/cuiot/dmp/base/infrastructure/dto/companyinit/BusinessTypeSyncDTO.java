package cn.cuiot.dmp.base.infrastructure.dto.companyinit;

import lombok.Data;

import java.io.Serializable;

/**
 * 业务类型表
 *
 * @author caorui
 * @date 2024/4/26
 */
@Data
public class BusinessTypeSyncDTO implements Serializable {

    /**
     * id
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
     * 层级类型(0:根节点，默认企业名称；最多可添加4级；)
     */
    private Byte levelType;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 名称路径（e.g.巡检>设备巡检）
     */
    private String pathName;

}
