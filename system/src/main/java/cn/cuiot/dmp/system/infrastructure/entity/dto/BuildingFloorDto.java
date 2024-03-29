package cn.cuiot.dmp.system.infrastructure.entity.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author wensq
 * @version 1.0
 * @description: 根据楼座获取小区dto
 * @date 2022/9/14 18:06
 */
@Data
public class BuildingFloorDto {

    /**
     * 楼层id
     */
    @NotNull(message = "楼座id不能为空")
    private String buildingId;

    private Long orgId;

    private Long userId;

    private String deptTreePath;
}
