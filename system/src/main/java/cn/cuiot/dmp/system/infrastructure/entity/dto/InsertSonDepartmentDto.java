package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.cuiot.dmp.common.log.intf.AbstractResourceParam;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author wensq
 * @version 1.0
 * @description: 新增子级组织
 * @date 2022/9/14 18:06
 */
@Data
public class InsertSonDepartmentDto extends AbstractResourceParam {

    /**
     * 组织名
     */
    @NotBlank(message = "组织名不能为空")
    private String departmentName;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 租户id
     */
    private Long pkOrgId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 父级组织id
     */
    @NotNull(message = "父级组织id不能为空")
    private Long parentId;

    /**
     * 备注
     */
    private String description;
}
