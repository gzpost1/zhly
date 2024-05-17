package cn.cuiot.dmp.system.infrastructure.entity.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 删除用户
 * @author zhh
 * @version 1.0
 * @date 2020/9/7 20:09
 */
@Data
public class DeleteUserDTO {
    /**
     * id集合
     */
    @NotEmpty(message = "请选择用户")
    private List<Long> ids;

    /**
     * 登陆人 orgId
     */
    private String loginOrgId;

    /**
     * 登陆人userid
     */
    private String loginUserId;
}
