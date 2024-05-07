package cn.cuiot.dmp.baseconfig.flow.dto.approval;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author pengjian
 * @create 2024/4/29 10:23
 */
@Data
public class MyApprovalResultDto {
    /**
     * 任务id
     */
    private  String taskId;

    /**
     * 任务实例id
     */
    private Long procInstId;

    /**
     *流程定义id
     */
    private String  procDefId;

    /**
     * 业务类型
     */
    private Byte businessType;

    /**
     * 所属组织
     */
    private Long orgId;

    /**
     * 所属组织名称
     */
    private String orgName;
    /**
     * 发起人id
     */
    private Long UserId;

    /**
     * 发起人名称
     */
    private String userName;

    /**
     * 发起时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 审批结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date entTime;
}
