package cn.cuiot.dmp.externalapi.service.query.gw.gasalarm;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 格物-燃气报警器 编辑dto
 *
 * @Author: zc
 * @Date: 2024-10-22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GwGasAlarmUpdateDto extends GwGasAlarmCreateDto {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 省电模式（0:PSM；1:DRX；2:eDRX；20:未开通）
     */
    private String powerSavingMode;

    /**
     * 消音(0关；1开)
     */
    private String mute;

    /**
     * 消音时长设置(0~65535)
     */
    private Integer muteTimeSet;
}
