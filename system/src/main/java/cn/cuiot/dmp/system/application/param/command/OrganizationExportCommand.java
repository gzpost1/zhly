package cn.cuiot.dmp.system.application.param.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Data;

/**
*
* @author zhangjg
* @date 2024/2/23 10:56
*/
@Data
public class OrganizationExportCommand {

    /**
     * 账户名称
     */
    private String orgName;

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
