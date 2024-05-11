package cn.cuiot.dmp.system.infrastructure.entity.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 移动用户
 * @author zhh
 * @version 1.0
 * @date 2020/9/7 20:09
 */
@Data
public class MoveUserDTO {

    /**
     * 组织id
     */
    @NotBlank(message = "请选择所属组织")
    private String deptId;


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
