package cn.cuiot.dmp.archive.application.param.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author caorui
 * @date 2024/5/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BuildingArchivesUpdateDTO extends BuildingArchivesCreateDTO {

    private static final long serialVersionUID = -6730898281989793348L;

    /**
     * 楼盘档案id
     */
    @NotNull(message = "楼盘档案id不能为空")
    private Long id;

}
