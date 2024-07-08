package cn.cuiot.dmp.app.dto;

import cn.cuiot.dmp.common.bean.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author wuyongchong
 * @since 2024-06-14
 */
@Getter
@Setter
public class UserFeedbackQuery extends PageQuery {

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 组织ID
     */
    private Long deptId;

    /**
     * 组织ID列表
     */
    private List<Long> deptIds;

    /**
     * 楼盘ID
     */
    private Long buildingId;

    /**
     * 楼盘ID列表
     */
    private List<Long> buildingIds;

    /**
     * 状态(0:待回复,1:已回复)
     */
    private Byte status;

    /**
    * 回复时间-开始时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startReplyTime;

    /**
     * 回复时间-开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endReplyTime;

    /**
     * 组织路径
     */
    private String deptPath;

    /**
     * 用户ID
     */
    private Long userId;
}
