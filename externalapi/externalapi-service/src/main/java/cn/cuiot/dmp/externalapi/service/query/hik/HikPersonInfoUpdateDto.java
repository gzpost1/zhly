package cn.cuiot.dmp.externalapi.service.query.hik;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 海康人员信息修改DTO
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HikPersonInfoUpdateDto extends HikPersonInfoCreateDto{

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;
}
