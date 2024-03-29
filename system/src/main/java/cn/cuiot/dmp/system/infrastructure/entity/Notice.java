package cn.cuiot.dmp.system.infrastructure.entity;

import cn.cuiot.dmp.common.serialize.LocalDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 公告信息对象 notice
 * 
 * @author shixh75
 * @date 2022-08-05
 */
@Data
public class Notice
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 租户id */
    private String orgId;

    /** 组织id */
    private String deptId;

    /** 所在组织的层级关系树 */
    private String deptTreePath;

    /** 公告内容 */
    private String noticeContent;

    /** 开始时间 */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /** 结束时间 */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /** 公告状态(0:未发布，1:发布) */
    private Integer status;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    /** 删除状态（0：未删除；1：已删除） */
    private Integer deleted;
}
