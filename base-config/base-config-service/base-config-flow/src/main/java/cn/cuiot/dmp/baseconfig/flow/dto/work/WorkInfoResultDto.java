package cn.cuiot.dmp.baseconfig.flow.dto.work;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.NodeButton;
import cn.cuiot.dmp.baseconfig.flow.entity.CommitProcessEntity;
import cn.cuiot.dmp.query.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author pengjian
 * @create 2024/4/25 15:53
 */
@Data
public class WorkInfoResultDto implements Serializable {


    private String id;

    /**
     * 业务类型
     */
    private Long businessType;

    /**
     * 业务类型名称
     */
    @Excel(name = "业务类型", orderNum = "1", width = 20)
    private String businessTypeName;

    /**
     * 流程定义id
     */
    private String processDefinitionId;

    /**
     * 所属组织
     */
    private Long org;

    /**
     * 组织名称
     */
    @Excel(name = "所属组织", orderNum = "2", width = 20)
    private String orgPath;


    /**
     * 工单名称
     */
    @Excel(name = "工单流程", orderNum = "3", width = 20)
    private String workName;


    /**
     * 工单来源 0 计划生成  1 自查报事2客户提单3代录工单
     */
    private Byte workSouce;

    @Excel(name = "工单来源", orderNum = "4", width = 20)
    private String workSource;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 状态 1已完结2进行中3已终止4已挂起5已撤回
     */

    private Byte status;

    @Excel(name = "工单状态", orderNum = "7", width = 20)
    private String statusName;

    /**
     * 创建时间
     */
    @Excel(name = "发起时间",orderNum = "6",  width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 操作时间
     */
    private Date businessTime;

    /**
     * 创建的用户id
     */
    private Long createUser;

    /**
     * 创建人名称
     */
    @Excel(name = "发起人", orderNum = "5", width = 20)
    private String userName;

    /**
     * 保单人id
     */
    private Long actualUserId;

    /**
     * 报单人名称
     */
    private String actualUserName;

    /**
     * 客户id
     */
    private Long customerId;

    /**
     * 报单客户名称
     */
    private String  customerName;

    /**
     * 报单人名称
     */
    private String actualUserPhone;

    /**
     * 流程实例id
     */
    @Excel(name = "工单编号", orderNum = "0", width = 20)
    private String procInstId;
    /**
     * 超时 0未超时 1超时
     */
    private  Byte timeOut;

    /**
     * 组织id信息
     */
    private List<Long> orgIds;

    /**
     * 工单耗时
     */
    private String workTime;

    /**
     * 企业id
     */
    private Long companyId;

    private Long configId;

    /**
     * 部门
     */
    private String deptIds;

    /**
     * 楼盘名称
     */
    private String propertyName;

    /**
     * 楼盘id
     */
    private String propertyId;

    /**
     * 关联工单
     */
    private List<Long> workOrderIds;


    /**
     * 是否可撤回 0 不可撤回 1 可撤回
     */
    private Byte revokeType;

    /**
     * 撤销节点
     */
    private String revokeNodeId;

    /**
     * 1 重新提交
     */
    private Byte resubmit;
    /**
     * 按钮信息
     */
    private List<NodeButton> buttons;
    /**
     * 0 未处理，1已处理
     */
    private Integer queryType;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 提交的信息
     */
    private CommitProcessEntity commitProcess;



}
