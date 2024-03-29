package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.cuiot.dmp.common.log.intf.AbstractResourceParam;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author wensq
 * @version 1.0
 * @description: 新建角色入参
 * @date 2022/9/14 18:06
 */
@Data
public class CreateRoleDto extends AbstractResourceParam {

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    /**
     * 备注
     */
    private String description;

    /**
     * 菜单 id集合
     */
    @NotEmpty(message = "角色权限配置不能为空")
    private List<String> menuIds;

    /**
     * 当前登录人租户id
     */
    private String loginOrgId;

    /**
     * 当前登录人id
     */
    private String loginUserId;
}
