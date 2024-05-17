package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.cuiot.dmp.common.bean.PageQuery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @description 账户下用户管理分页列表请求参数
 * @author pengcg
 * @date 2023/11/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDataReqDTO extends PageQuery implements Serializable {

    private static final long serialVersionUID = -1928525514775914837L;

    /**
     * 账户id
     */
    @NotNull(message = "账户id不能为空")
    private Long orgId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 登录人userId
     */
    @JsonIgnore
    private String sessionUserId;

    /**
     * 登陆人orgId
     */
    @JsonIgnore
    private String sessionOrgId;

}
