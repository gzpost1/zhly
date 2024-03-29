package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author wqd
 * @classname RegionDetailReqDto
 * @description
 * @date 2023/1/31
 */
@Data
public class RegionDetailReqDto {

    @JsonIgnore
    private String orgId;

    @JsonIgnore
    private String userId;

    /**
     * 区域id
     */
    @NotNull(message = "id为空")
    private Long regionId;
}
