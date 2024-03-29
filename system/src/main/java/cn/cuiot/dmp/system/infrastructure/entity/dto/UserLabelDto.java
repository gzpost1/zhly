package cn.cuiot.dmp.system.infrastructure.entity.dto;


import java.time.LocalDateTime;
import lombok.Data;


/**
 * @author lx
 * UserLabel信息实体
 */
@Data
public class UserLabelDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 标签id
     */
    private Integer labelId;

    /**
     * 标签名称
     */
    private String labelName;

    /**
     * 其它标签名称
     */
    private String otherLabelName;

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
