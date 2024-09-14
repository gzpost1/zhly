package cn.cuiot.dmp.externalapi.service.query.gw;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 格物门禁-操作Dto
 *
 * @Author: zc
 * @Date: 2024-09-11
 */
@Data
public class GwEntranceGuardOperationDto {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 备注
     */
    @NotBlank(message = "原因不能为空")
    private String description;
}
