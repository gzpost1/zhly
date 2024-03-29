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
     * 账户名称
     */
    private String orgName;

    /**
     * 当前页数
     */
    @NotNull(message = "当前页数不能为空")
    private Integer currentPage = 1;

    /**
     * 每页数量
     */
    @NotNull(message = "每页数量不能为空")
    private Integer pageSize = 10;

    /**
     *  当前登录账户orgId
     **/
    private Long loginOrgId;

    /**
     * 组织id
     */
    private String deptId;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 统一社会信用代码
     */
    private String socialCreditCode;

    /**
     * 组织ids(内部用)
     */
    @JsonIgnore
    private List<String> deptIds;

    /**
     * 租户id
     */
    @JsonIgnore
    private String orgId;
}
