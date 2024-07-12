package cn.cuiot.dmp.baseconfig.flow.dto.work;

import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.ChildNode;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.NodeButton;
import cn.cuiot.dmp.baseconfig.flow.entity.CommitProcessEntity;
import cn.cuiot.dmp.query.PageQuery;
import lombok.Data;
import org.jpedal.parser.shape.S;

import java.util.Date;
import java.util.List;

/**
 * @author pengjian
 * @create 2024/4/25 15:53
 */
@Data
public class WorkInfoDto extends PageQuery {


    private String id;

    /**
     * 业务类型
     */
    private Long businessType;

    /**
     * 业务类型名称
     */
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
    private String orgPath;


    /**
     * 工单名称
     */
    private String workName;


    /**
     * 工单来源 0 计划生成  1 自查报事2客户提单3代录工单
     */
    private Byte workSouce;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 状态 1已完结2进行中3已终止4已挂起5已撤回
     */
    private Byte status;

    /**
     * 创建时间
     */

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
