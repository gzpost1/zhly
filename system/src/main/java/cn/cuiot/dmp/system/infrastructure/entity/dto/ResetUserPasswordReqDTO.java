package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @description 账户下用户管理重置密码请求参数
 * @author pengcg
 * @date 2023/11/21
 */
@Data
public class ResetUserPasswordReqDTO implements Serializable {

    private static final long serialVersionUID = 3924336904887288688L;

    /**
     * 用户表主键id
     */
    @NotNull(message = "用户主键id不能为空")
    private Long id;

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
