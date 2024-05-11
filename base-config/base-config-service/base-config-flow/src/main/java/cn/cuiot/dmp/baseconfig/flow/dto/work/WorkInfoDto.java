package cn.cuiot.dmp.baseconfig.flow.dto.work;

import cn.cuiot.dmp.query.PageQuery;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author pengjian
 * @create 2024/4/25 15:53
 */
@Data
public class WorkInfoDto extends PageQuery {

    /**
     * 工单id
     */
    private Long id;

    /**
     * 业务类型
     */
    private Long businessType;

    /**
     * 业务类型名称
     */
    private String businessTypeName;


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
     * 工单来源
     */
    private Byte workSouce;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 状态
     */
    private Byte status;

    /**
     * 创建时间
     */

    private Date createTime;

    /**
     * 创建的用户id
     */
    private Long createUser;

    /**
     * 创建人名称
     */
    private String userName;

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
}
