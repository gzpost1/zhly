package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.cuiot.dmp.common.log.intf.AbstractResourceParam;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 角色修改入参
 * @author wensq
 * @version 1.0
 * @date 2022/9/14 18:06
 */
@Data
public class UpdateRoleDto extends AbstractResourceParam {

    /**
     * 角色主键id
     */
    @NotNull(message = "主键id不能为空")
    private Long id;

    /**
     * 角色名称
     */
    @NotBlank(message = "请输入角色名称")
    @Length(max = 30,message = "角色名称不可超过30字")
    private String roleName;

    /**
     * 备注
     */
    @Length(max = 200,message = "备注限200字")
    private String description;

    /**
     * 菜单 id集合
     */
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
