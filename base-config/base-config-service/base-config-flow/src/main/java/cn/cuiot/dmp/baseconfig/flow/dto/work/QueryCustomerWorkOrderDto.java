package cn.cuiot.dmp.baseconfig.flow.dto.work;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @create 2024/6/13 9:29
 */
@Data
public class QueryCustomerWorkOrderDto  extends PageQuery {

    /**
     * 楼盘id
     */
    private List<Long> propertyIds;

    /**
     * 工单来源 2客户提单3代录工单
     */
    private Byte workSource;

    /**
     * 业务类型
     */
    private Long businessType;

    /**
     * 工单流程
     */
    private String workName;

    /**
     * 状态 1已完结2进行中3已终止4已挂起5已撤回
     */
    private Byte status;

    /**
     * 工单id
     */
    private String procInstId;

    /**
     * 发起人
     */
    private Long createUser;

    /**
     * 超时 0 未超时 1已超时
     */
    private Byte timeOut;
}
