package cn.cuiot.dmp.system.infrastructure.entity.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jz
 * @classname DeptTreeReqDto
 * @description 组织树查询响应dto
 * @date 2023/04/07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeptTreeResDto {

    /**
     * 组织id（不传参则查询当前用户层级，传参则查询下级）
     */
    private String deptId;

    /**
     * 组织名称
     */
    private String deptName;

    /**
     * 组织树
     */
    private String deptTreePath;

    /**
     * 组织分组（1.系统，2.租户，3.小区，4.楼栋，5.房屋，6.区域，7.楼层）
     */
    private String dGroup;

    /**
     * 是否存在下级组织（0.没有下级，1.有下级）
     */
    private String hasChild;

    /**
     * 下级组织列表
     */
    private List<DeptTreeResDto> childList;

    /**
     * 父节点id
     */
    private String parentId;

    /**
     * 节点层级
     */
    private String level;

    /**
     * 账户标签（9：厂园区13：联通管理方）
     */
    private String orgLabel;

}
