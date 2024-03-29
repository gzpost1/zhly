package cn.cuiot.dmp.system.infrastructure.entity;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 公告用户中间对象 notice_user_access
 * 
 * @author zhangxp207
 * @date 2022-08-05
 */
@Data
public class NoticeUserAccess
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 公告id */
    private String noticeId;

    /** 用户id */
    private String userId;

    /** 来源 */
    private String source;

    /** 删除标志(0：未删除，1：已删除) */
    private Integer deleted;

    private String createBy;
    private LocalDateTime createTime;

}
