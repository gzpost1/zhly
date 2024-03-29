package cn.cuiot.dmp.system.infrastructure.entity.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author hk
 * @version 1.0
 * @description: 厂园区楼层删除入参
 * @date 2023/1/12 10:06
 */
@Data
public class FactoryParkFloorDelDto {

    /**
     * 楼层id
     */
    @NotNull(message = "楼层id不能为空")
    private String floorId;

    private Long orgId;

    private Long userId;
}
