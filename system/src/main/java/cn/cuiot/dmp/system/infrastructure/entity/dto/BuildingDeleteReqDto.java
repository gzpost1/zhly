package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author wqd
 * @classname BuildingDeleteReqDto
 * @description
 * @date 2023/1/16
 */
@Data
public class BuildingDeleteReqDto {


    @JsonIgnore
    private String orgId;

    @JsonIgnore
    private String userId;

    /**
     * 楼栋id
     */
    @NotEmpty(message = "id为空")
    private List<Long> buildingIds;

}
