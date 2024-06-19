package cn.cuiot.dmp.base.infrastructure.dto.req;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 组织部门查询参数
 *
 * @author: wuyongchong
 * @date: 2024/5/10 10:08
 */
@Data
@Accessors(chain = true)
public class DepartmentReqDto implements Serializable {

    /**
     * 部门ID列表
     */
    private List<Long> deptIdList;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 是否也返回自己
     */
    private Boolean selfReturn;

    /**
     * 返回类型  1-直接子部门  空或其他-所有子部门
     */
    private Integer returnType;
}
