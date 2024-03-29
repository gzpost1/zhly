package cn.cuiot.dmp.system.infrastructure.entity.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author hk
 * @version 1.0
 * @description: 厂园区楼层批量删除入参
 * @date 2023/1/30 10:06
 */
@Data
public class FactoryParkFloorBatchDelDto {

    /**
     * 楼层id
     */
    @NotNull(message = "楼层id不能为空")
    private List<Long> floorIds;

    private Long orgId;

    private Long userId;
}
