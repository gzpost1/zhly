package cn.cuiot.dmp.externalapi.service.query;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 人员分组 DTO
 *
 * @Author: zc
 * @Date: 2024-09-04
 */
@Data
public class PersonGroupCreateDTO {

    /**
     * 分组名称
     */
    @NotBlank(message = "分组名称不能为空")
    private String name;
}
