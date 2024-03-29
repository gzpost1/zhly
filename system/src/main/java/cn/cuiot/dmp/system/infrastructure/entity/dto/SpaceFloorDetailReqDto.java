package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 空间楼层dto
 *
 * @author lixf
 */
@Data
public class SpaceFloorDetailReqDto {
    /**
     * 楼栋id
     */
    @NotNull(message = "楼栋id不能为空")
    private Long buildingId;

    /**
     * 用户id
     */
    @JsonIgnore
    private String userId;

    /**
     * 用户租户id
     */
    @JsonIgnore
    private String orgId;

    /**
     * 环境总览标志 1（环境总览）
     */
    @JsonIgnore
    private Integer envFlag;
}
