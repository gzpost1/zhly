package cn.cuiot.dmp.content.dal.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import cn.cuiot.dmp.base.infrastructure.persistence.handler.JsonTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/24 10:29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_content_notice", autoResultMap = true)
public class ContentNoticeEntity extends YjBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 发布端
     */
    private Byte publishSource;

    /**
     * 组织
     */
    @TableField(typeHandler = JsonTypeHandler.class)
    private List<String> departments;

    /**
     * 楼盘
     */
    @TableField(typeHandler = JsonTypeHandler.class)
    private List<String> buildings;

    /**
     * 标题
     */
    private String title;

    /**
     * 公告类型
     */
    private String type;

    /**
     * 公告摘要
     */
    private String digest;

    /**
     * 生效开始时间
     */
    private Date effectiveStartTime;

    /**
     * 生效结束时间
     */
    private Date effectiveEndTime;

    /**
     * 消息通知
     */
    private Byte inform;

    /**
     * 公告详情
     */
    private String detail;

    /**
     * 状态
     */
    private Byte status;

    /**
     * 审核状态
     */
    private Byte auditStatus;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 发布状态
     */
    private Byte publishStatus;

    /**
     * 企业ID
     */
    private Long companyId;
}
