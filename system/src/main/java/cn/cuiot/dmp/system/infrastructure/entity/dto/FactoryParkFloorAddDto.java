package cn.cuiot.dmp.system.infrastructure.entity.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author hk
 * @version 1.0
 * @description: 厂园区楼层新增入参
 * @date 2023/1/12 10:06
 */
@Data
public class FactoryParkFloorAddDto {

    /**
     * 选择空间树节点deptId
     */
    private Long deptId;

    /**
     * 组织树
     */
    @NotNull(message = "组织树不能为空")
    private String deptTreePath;

    /**
     * 园区id
     */
    @NotNull(message = "园区id不能为空")
    private String parkId;

    /**
     * 区域名称
     */
    private String regionName;

    /**
     * 区域Id
     */
    private String regionId;

    /**
     * 楼座id
     */
    @NotNull(message = "楼座id不能为空")
    private String buildingId;

    /**
     * 当前楼层
     */
    @NotNull(message = "当前楼层不能为空")
    private String currentFloor;

    /**
     * 楼层名称
     */
    @NotNull(message = "楼层名称不能为空")
    private String floorName;

    /**
     * 备注
     */
    private String description;

    private Long orgId;

    private Long userId;
}
