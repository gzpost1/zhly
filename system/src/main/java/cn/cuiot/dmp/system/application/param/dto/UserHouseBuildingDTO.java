package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;

import java.io.Serializable;

/**
 *
 *
 * @author caorui
 * @date 2024/6/17
 */
@Data
public class UserHouseBuildingDTO implements Serializable {

    private static final long serialVersionUID = -4589529148544688602L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 楼盘ID
     */
    private Long buildingId;

    /**
     * 楼盘名称
     */
    private String buildingName;

}
