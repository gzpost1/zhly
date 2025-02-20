package cn.cuiot.dmp.lease.dto.clue;

import cn.cuiot.dmp.common.bean.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author caorui
 * @date 2024/6/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CluePageQueryDTO extends PageQuery {

    private static final long serialVersionUID = 6310314043408388182L;

    /**
     * 线索名称
     */
    private String name;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 楼盘ID
     */
    private Long buildingId;

    /**
     * 线索来源（系统配置自定义）
     */
    private Long sourceId;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 当前跟进人ID
     */
    private Long currentFollowerId;

    /**
     * 跟进状态（系统配置自定义）
     */
    private Long currentFollowStatusId;

    /**
     * 跟进开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date followBeginTime;

    /**
     * 跟进结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date followEndTime;

    /**
     * 未跟进天数类型
     * @see cn.cuiot.dmp.lease.enums.ClueFollowDayEnum
     */
    private Byte clueFollowDay;

    /**
     * 线索结果（系统配置自定义）
     */
    private Long resultId;

    /**
     * 完成人ID
     */
    private Long finishUserId;

    /**
     * 完成开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishBeginTime;

    /**
     * 完成结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishEndTime;

    /**
     * 线索状态（1待分配，2跟进中，3已完成）
     */
    private Byte status;

    /**
     * 当前登录人ID（我的线索需要筛选）
     */
    private Long currentUserId;

}
