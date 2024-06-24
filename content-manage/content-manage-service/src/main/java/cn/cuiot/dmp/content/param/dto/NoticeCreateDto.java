package cn.cuiot.dmp.content.param.dto;//	模板

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/29 17:25
 */
@Data
public class NoticeCreateDto {

    /**
     * 发布端
     */
    private Byte publishSource;

    /**
     * 组织
     */
    private List<Long> departments;

    /**
     * 楼盘
     */
    private List<Long> buildings;

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
    @NotNull(message = "生效开始时间不能为空")
    private Date effectiveStartTime;

    /**
     * 生效结束时间
     */
    @NotNull(message = "生效结束时间不能为空")
    private Date effectiveEndTime;

    /**
     * 消息通知
     */
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
     * 发布状态
     */
    private Byte publishStatus;

    /**
     * 企业ID
     */
    private Long companyId;
}
