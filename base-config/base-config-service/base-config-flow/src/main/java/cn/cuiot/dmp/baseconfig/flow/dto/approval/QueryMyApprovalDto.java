package cn.cuiot.dmp.baseconfig.flow.dto.approval;

import cn.cuiot.dmp.common.bean.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author pengjian
 * @create 2024/4/29 10:22
 */
@Data
public class QueryMyApprovalDto extends PageQuery {

    /**
     * 业务类型
     */
    private Long businessType;

    /**
     * 审批人id
     */
    private Long assignee;
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
    private String procInstId;

    /**
     * 查询类型
     */
    private Integer queryType;

    /**
     * 抄送人userId
     */
    private Long makeUserId;

    /**
     * 批量组织信息
     */
    private List<Long> orgIds;
    /**
     * 是否超时 0 未超时  1 已超时
     */
    private Byte timeOut;

    /**
     *1已完结2进行中3已终止4已挂起5已撤回
     */
    private Byte status;

}
