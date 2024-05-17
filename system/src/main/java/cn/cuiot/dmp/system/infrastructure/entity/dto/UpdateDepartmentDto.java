package cn.cuiot.dmp.system.infrastructure.entity.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author wen
 * @Description 修改组织入参
 * @Date 2021/12/27 9:31
 * @param
 * @return
 **/
@Data
public class UpdateDepartmentDto extends InsertDepartmentDto {

    /**
     * 主键id
     */
    @NotNull(message = "主键id不能为空")
    private Long id;

    private String orgId;

    private String description;

    private String userId;
}
