package cn.cuiot.dmp.externalapi.service.query.gw.waterleachalarm;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 修改属性DTO
 *
 * @Author: zc
 * @Date: 2024-10-23
 */
@Data
public class GwWaterLeachAlarmPropertyDto {

    /**
     * 楼盘id
     */
    @NotNull(message = "所属楼盘不能为空")
    private Long building;

    /**
     * 省电模式（0:PSM，1:DXR，2:eDRX，20:未开通）
     */
    @NotNull(message = "省电模式不能为空")
    private String powerSavingMode;
}
