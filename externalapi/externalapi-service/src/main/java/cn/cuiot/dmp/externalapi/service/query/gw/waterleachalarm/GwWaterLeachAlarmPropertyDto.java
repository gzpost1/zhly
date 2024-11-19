package cn.cuiot.dmp.externalapi.service.query.gw.waterleachalarm;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

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
    @NotEmpty(message = "id不能为空")
    @Size(min = 1)
    private List<Long> ids;

    /**楼盘id*/
    private Long buildingId;

    /**
     * 省电模式（0:PSM，1:DXR，2:eDRX，20:未开通）
     */
    private String powerSavingMode;
}
