package cn.cuiot.dmp.baseconfig.flow.dto.app.query;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @create 2024/6/3 15:22
 */
@Data
public class WorkOrderSuperQuery extends PageQuery {

    /**
     *审批人
     */
    private Long assignee;

    /**
     * 流程名称
     */
    private String workName;

    /**
     * 业务类型
     */
    private Long businessType;

    /**
     * 所属组织
     */
    private List<Long> orgIds;

    /**
     * 工单状态 1已完结2进行中3已终止4已挂起5已撤回
     */
    private Byte status;

    /**
     * 工单来源 0 计划生成  1 自查报事2客户提单3代录工单
     */
    private Byte  workSource;

    /**
     * 是否超时 是否超时 0 未超时  1 已超时
     */
    private Byte timeOut;

    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * 0 未处理 1已处理 0 待审批 1审批
     */
    private String queryType;

}
