package cn.cuiot.dmp.baseconfig.flow.dto.approval;

import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.NodeButton;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

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
     * 节点id
     */
    private String taskDefKey;

    /**
     * 业务类型
     */
    private Long businessType;

    /**
     *工单名称
     */
    private String workName;

    /**
     * 业务类型名称
     */
    private String businessTypeName;

    /**
     * 所属组织
     */
    private Long orgId;

    private String orgPath;

    /**
     * 所属组织名称
     */
    private String orgName;
    /**
     * 发起人id
     */
    private Long userId;

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

    /**
     * 企业id
     */
    private Long companyId;

    private Byte state;

    /**
     * 流程关联的组织
     */
    private String orgIds;

    /**
     * 节点按钮配置
     */
    private List<NodeButton> buttons;
}
