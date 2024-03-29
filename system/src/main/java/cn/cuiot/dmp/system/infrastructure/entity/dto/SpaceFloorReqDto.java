package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 空间楼栋dto
 *
 * @author lixf
 */
@Data
public class SpaceFloorReqDto {

    /**
     * 空间id
     */
    @NotNull(message = "id不能为空")
    private Long id;

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
}
