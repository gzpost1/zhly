package cn.cuiot.dmp.externalapi.service.query.video;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 监控 DTO
 *
 * @Author: zc
 * @Date: 2024-09-04
 */
@Data
public class VideoUpdateDTO {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 设备名称
     */
    @NotBlank(message = "设备名称不能为空")
    private String deviceName;

    /**
     * 楼盘ID
     */
    @NotNull(message = "楼盘ID不能为空")
    private Long buildingId;
}
