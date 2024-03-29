package cn.cuiot.dmp.system.infrastructure.entity.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hk
 * @classname SpaceTreeResDto
 * @description 空间组织树查询响应dto
 * @date 2023/04/07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpaceTreeResDto {

    /**
     * 空间组织id（不传则使用当前用户组织id）
     */
    private String spaceId;

    /**
     * 组织id
     */
    private String selfDeptId;

    /**
     * 空间组织名称
     */
    private String spaceName;

    /**
     * 空间组织树
     */
    private String spaceTreePath;

    /**
     * 是否存在下级组织（0.没有下级，1.有下级）
     */
    private String hasChild;

    /**
     * 组织分组（1.系统，2.租户，3.小区，4.楼栋，5.房屋，6.区域，7.楼层）
     */
    private Integer dGroup;

    /**
     * 下级组织
     */
    private List<SpaceTreeResDto> childList;
}
