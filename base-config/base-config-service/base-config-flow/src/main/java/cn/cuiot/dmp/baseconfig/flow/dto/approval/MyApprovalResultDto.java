package cn.cuiot.dmp.baseconfig.flow.dto.approval;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "工单编号", orderNum = "0", width = 20)
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
    @Excel(name = "工单流程", orderNum = "3", width = 20)
    private String workName;

    /**
     * 业务类型名称
     */
    @Excel(name = "业务类型", orderNum = "1", width = 20)
    private String businessTypeName;

    /**
     * 所属组织
     */
    private Long orgId;

    @Excel(name = "所属组织", orderNum = "2", width = 20)
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
    @Excel(name = "发起人", orderNum = "4", width = 20)
    private String userName;

    /**
     * 发起时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "发起时间",orderNum = "5",  width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 审批结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "节点完成时间",orderNum = "6", width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date entTime;

    /**
     * 企业id
     */
    private Long companyId;

    private Byte state;

    /**
     * 状态名称
     */
    @Excel(name = "工单状态",orderNum = "7",  width = 20)
    private String statusName;

    /**
     * 流程关联的组织
     */
    private String orgIds;

    /**
     * 节点按钮配置
     */
    private List<NodeButton> buttons;
}
