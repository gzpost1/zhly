package cn.cuiot.dmp.system.infrastructure.entity.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * OrgMenuDto
 * @author: wuyongchong
 * @date: 2024/4/30 12:57
 */
@Data
public class OrgMenuDto implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 企业账户ID
     */
    private Long orgId;

    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人ID
     */
    private Long createBy;

    public OrgMenuDto() {
    }
    public OrgMenuDto(Long id, Long orgId, Long menuId, LocalDateTime createTime,
            Long createBy) {
        this.id = id;
        this.orgId = orgId;
        this.menuId = menuId;
        this.createTime = createTime;
        this.createBy = createBy;
    }
}
