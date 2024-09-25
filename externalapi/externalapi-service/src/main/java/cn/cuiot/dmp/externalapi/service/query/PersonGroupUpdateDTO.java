package cn.cuiot.dmp.externalapi.service.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 人员分组 DTO
 *
 * @Author: zc
 * @Date: 2024-09-04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PersonGroupUpdateDTO extends PersonGroupCreateDTO {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;
}
