package cn.cuiot.dmp.system.infrastructure.entity.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * 企业类型菜单
 *
 * @author: wuyongchong
 * @date: 2024/5/6 11:22
 */
@Data
public class OrgTypeMenuDto implements Serializable {

    private Long id;
    private Long menuId;
    private Long orgTypeId;

    public OrgTypeMenuDto() {

    }

    public OrgTypeMenuDto(Long id, Long menuId, Long orgTypeId) {
        this.id = id;
        this.menuId = menuId;
        this.orgTypeId = orgTypeId;
    }
}
