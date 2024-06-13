package cn.cuiot.dmp.baseconfig.flow.dto.app.query;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author pengjian
 * @create 2024/6/12 10:13
 */
@Data
public class RepairReportQuery extends PageQuery {

    /**
     * 楼盘id
     */
    @NotNull(message = "楼盘id不能为空")
    private Long propertyId;

    /**
     * 状态 0待处理 1已完成 3已撤回
     */
    private String status;

    /**
     * 被报单人
     */
    private Long userId;
}
