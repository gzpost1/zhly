package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author wensq
 * @version 1.0
 * @description: 账户管理分页查询 入参
 * @date 2022/9/14 18:06
 */
@Data
public class ListOrganizationDto{

    /**
     * 企业名称
     */
    private String orgName;

    /**
     * 当前页数
     */
    @NotNull(message = "当前页数不能为空")
    private Integer pageNo = 1;

    /**
     * 每页数量
     */
    @NotNull(message = "每页数量不能为空")
    private Integer pageSize = 10;

    /**
     * 组织id
     */
    private String deptId;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 启用状态 1-启用 0-停用
     */
    private Byte status;


    /**
     * 企业状态 1-正常 2-已过期
     */
    private Byte orgStatus;

    /**
     * 组织ids(内部用)
     */
    @JsonIgnore
    private List<String> deptIds;

    /**
     * 租户id(内部用)
     */
    @JsonIgnore
    private String orgId;

    /**
     *  当前登录账户orgId(内部用)
     **/
    private Long loginOrgId;

    /**
     *  当前登录账户ID(内部用)
     **/
    private Long loginUserId;

}
