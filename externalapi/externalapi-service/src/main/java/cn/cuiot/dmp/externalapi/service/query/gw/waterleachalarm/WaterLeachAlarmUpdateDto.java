package cn.cuiot.dmp.externalapi.service.query.gw.waterleachalarm;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 格物-水浸报警器 编辑dto
 *
 * @Author: zc
 * @Date: 2024-10-22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WaterLeachAlarmUpdateDto extends WaterLeachAlarmCreateDto {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 省电模式（0:PSM；1:DRX；2:eDRX；20:未开通）
     */
    private String powerSavingMode;
}
