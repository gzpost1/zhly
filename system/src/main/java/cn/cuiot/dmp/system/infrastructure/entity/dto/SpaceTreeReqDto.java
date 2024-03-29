package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hk
 * @classname SpaceTreeReqDto
 * @description 空间树查询请求dto
 * @date 2023/04/07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpaceTreeReqDto {

    /**
     * 空间组织id
     */
    private String spaceId;

    /**
     * true只查询当前层级
     */
    private Boolean init = false;

    /**
     * 租户id
     */
    @JsonIgnore
    private String orgId;

    /**
     * 用户id
     */
    @JsonIgnore
    private String userId;

}
