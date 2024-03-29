package cn.cuiot.dmp.system.infrastructure.entity.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author wqd
 * @classname GetUserDepartmentTreeLazyByNameReqDto
 * @description
 * @date 2022/6/7
 */
@Data
public class GetDepartmentTreeLazyByNameReqDto {

    @NotBlank(message = "组织名不可为空")
    private String departmentName;

    private String deptTreePath;

    private String orgId;

    private String userId;

}
