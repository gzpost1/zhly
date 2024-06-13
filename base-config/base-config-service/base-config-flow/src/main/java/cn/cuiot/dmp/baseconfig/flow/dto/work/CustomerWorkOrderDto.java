package cn.cuiot.dmp.baseconfig.flow.dto.work;

import lombok.Data;

import java.util.Date;

/**
 * @author pengjian
 * @create 2024/6/13 9:24
 */
@Data
public class CustomerWorkOrderDto {

    /**
     * 工单id
     */
    private Long procInstId;

    /**
     * 业务类型名称
     */
    private String businessTypeName;

    /**
     * 业务类型
     */
    private Long businessType;

    /**
     * 楼盘id
     */
    private Long propertyId;
    /**
     * 楼盘名称
     */
    private String propertyName;

    /**
     * 报单客户id
     */
    private Long actualUserId;

    /**
     * 报单客户名称
     */
    private String actualUserName;

    /**
     * 工单名称
     */
    private String workName;

    /**
     * 工单来源 2客户提单3代录工单
     */
    private Byte workSource;

    /**
     * 创建人名称
     */
    private Long createUser;

    /**
     * 发起人名称
     */
    private String createUserName;

    /**
     * 创建时间
     */

    private Date createTime;

    /**
     * 状态 1已完结2进行中3已终止4已挂起5已撤回
     */
    private Byte status;
}
