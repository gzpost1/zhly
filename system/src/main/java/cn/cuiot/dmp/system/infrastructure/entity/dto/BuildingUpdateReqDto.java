package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author wqd
 * @classname BuildingUpdateReqDto
 * @description
 * @date 2023/1/13
 */
@Data
public class BuildingUpdateReqDto {


    @JsonIgnore
    private String orgId;

    @JsonIgnore
    private String userId;

    /**
     * 楼栋id
     */
    @NotNull(message = "id为空")
    private Long buildingId;

    /**
     * 园区id
     */
    private Long parkId;

    /**
     * 区域id
     */
    private Long regionId;

    /**
     * 楼栋名
     */
    @NotBlank(message = "楼栋名称为空")
    @Length(max = 32, message = "楼栋名称超长")
    private String buildingName;

    /**
     * 楼栋层数
     */
    @NotNull(message = "层数为空")
    @Min(value = 1, message = "层数最小为1")
    @Max(value = 100, message = "层数最大为100")
    private Integer buildingFloor;

    /**
     * 备注
     */
    @Length(max = 64, message = "备注长度超长")
    private String description;
}
