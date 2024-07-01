package cn.cuiot.dmp.lease.dto.charge;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 收费管理-催缴管理 分页query
 *
 * @Author: zc
 * @Date: 2024-06-25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChargeCollectionManageQuery extends PageQuery {

    /**
     * 客户名称
     */
    private String customerUserName;

    /**
     * 客户手机号
     */
    private String customerUserPhone;

    /**
     * 企业ID 前端不用传
     */
    private Long companyId;

    /**
     * 日期：前端不需要传
     */
    private Date dueDate;
}