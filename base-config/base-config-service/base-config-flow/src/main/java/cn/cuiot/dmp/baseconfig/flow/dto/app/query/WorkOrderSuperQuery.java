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
     * 工单来源
     */
    private Byte  workSource;

    /**
     * 是否超时 是否超时 0 未超时  1 已超时
     */
    private Byte timeOut;

}
