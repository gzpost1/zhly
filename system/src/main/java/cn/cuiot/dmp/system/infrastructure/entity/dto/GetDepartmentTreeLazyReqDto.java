package cn.cuiot.dmp.system.infrastructure.entity.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author wqd
 * @classname GetDepartmentTreeLazyReqDto
 * @description
 * @date 2022/5/17
 */
@Data
public class GetDepartmentTreeLazyReqDto {

    /**
     * init:查询根节点,children:查询子集
     */
    @NotBlank(message = "type为空")
    private String type;

    /**
     * 父节点id,查询子集必传
     */
    private Long parentId;

    /**
     * 登录人userId
     */
    private String loginUserId;

    /**
     * 登录orgId
     */
    private String loginOrgId;

    private String deptTreePath;
}
