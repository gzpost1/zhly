package cn.cuiot.dmp.externalapi.service.query.gw;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 格物门禁 DTO
 *
 * @Author: zc
 * @Date: 2024-09-06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GwEntranceGuardUpdateDto extends GwEntranceGuardCreateDto {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;
}
