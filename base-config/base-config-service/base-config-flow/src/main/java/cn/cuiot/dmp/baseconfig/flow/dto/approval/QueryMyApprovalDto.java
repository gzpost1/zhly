package cn.cuiot.dmp.baseconfig.flow.dto.approval;

import cn.cuiot.dmp.query.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author pengjian
 * @create 2024/4/29 10:22
 */
@Data
public class QueryMyApprovalDto extends PageQuery {

    /**
     * 业务类型
     */
    private Byte businessType;

    /**
     * 审批人id
     */
    private String assignee;
    /**
     * 所属组织
     */
    private Long orgId;

    /**
     * 流程名称
     */
    private String workName;

    /**
     * 发起人userId
     */
    private Long createUser;

    /**
     * 发起开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 发起结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 工单编号
     */
    private Long procInstId;

    /**
     * 查询类型
     */
    private Integer queryType;

    /**
     * 抄送人userId
     */
    private Long makeUserId;

}