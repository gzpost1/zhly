package cn.cuiot.dmp.system.infrastructure.entity.dto;


import java.time.LocalDateTime;
import lombok.Data;


/**
 * @author lx
 * OrgLabel信息实体
 */
@Data
public class OrgLabelDto {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 账户id
     */
    private String orgId;

    /**
     * 标签id
     */
    private Integer labelId;

    /**
     * 标签名称
     */
    private String labelName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建者
     */
    private String createdBy;

    /**
     * 更新者
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedOn;
}
