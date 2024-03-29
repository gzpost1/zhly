package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author wqd
 * @classname BuildingDetailReqDto
 * @description
 * @date 2023/1/31
 */
@Data
public class BuildingDetailReqDto {

    @JsonIgnore
    private String orgId;

    @JsonIgnore
    private String userId;

    /**
     * 楼栋id
     */
    @NotNull(message = "id为空")
    private Long buildingId;


}
