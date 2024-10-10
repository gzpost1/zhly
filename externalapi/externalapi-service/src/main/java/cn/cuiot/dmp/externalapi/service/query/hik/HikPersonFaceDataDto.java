package cn.cuiot.dmp.externalapi.service.query.hik;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 海康人员信息新增DTO
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Data
public class HikPersonFaceDataDto {
    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 人员照片
     */
    @NotBlank(message = "人员照片不能为空")
    private String faceData;
}
