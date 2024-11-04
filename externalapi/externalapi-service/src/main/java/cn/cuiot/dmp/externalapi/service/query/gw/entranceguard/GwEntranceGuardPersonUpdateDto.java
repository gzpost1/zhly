package cn.cuiot.dmp.externalapi.service.query.gw.entranceguard;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 格物门禁 人员分组 DTO
 *
 * @Author: zc
 * @Date: 2024-09-07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GwEntranceGuardPersonUpdateDto extends GwEntranceGuardPersonCreateDto {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;
}
