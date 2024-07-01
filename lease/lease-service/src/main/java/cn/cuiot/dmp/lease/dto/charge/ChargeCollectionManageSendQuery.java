package cn.cuiot.dmp.lease.dto.charge;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 收费管理-催款管理发送消息query
 *
 * @Author: zc
 * @Date: 2024-06-24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChargeCollectionManageSendQuery extends PageQuery {
    /**
     * 消息发送类型（1：系统消息；2：短信）
     */
    @NotNull(message = "消息发送类型不能为空")
    private Byte msgType;

    /**
     * 客户ids
     */
    private List<Long> customerUserIds;

    /**
     * 操作类型 前端不用传
     */
    private Byte operationType;

    /**
     * 企业ID 前端不用传
     */
    private Long companyId;

    /**
     * 应收日期 前端不用传
     */
    private Date dueDate;

    /**
     * 计划id 前端不用传
     */
    private Long planId;
}