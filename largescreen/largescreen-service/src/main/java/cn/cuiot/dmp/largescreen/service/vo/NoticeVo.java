package cn.cuiot.dmp.largescreen.service.vo;//	模板

import cn.cuiot.dmp.base.infrastructure.persistence.handler.ByteListHandler;
import cn.cuiot.dmp.base.infrastructure.persistence.handler.JsonTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/29 14:39
 */
@Data
public class NoticeVo implements Serializable {


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
    @TableField(typeHandler = ByteListHandler.class)
    private List<Byte> inform;

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
     * 发布状态
     */
    private Byte publishStatus;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 是否已通知
     */
    private Byte noticed;


    private List<String> departmentNames;

    private List<String> buildingNames;

    private String creatUserName;

    private ContentAuditVO contentAudit;

    private Byte effectiveStatus;

}
