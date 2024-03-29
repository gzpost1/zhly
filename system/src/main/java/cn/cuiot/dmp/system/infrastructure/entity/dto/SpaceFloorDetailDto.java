package cn.cuiot.dmp.system.infrastructure.entity.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 空间楼层dto
 *
 * @author lixf
 */
@NoArgsConstructor
@Data
public class SpaceFloorDetailDto {

    /**
     * 楼层名称
     */
    private String name;

    /**
     * 是否有权限
     */
    private Boolean auth;

    /**
     * 楼层房屋类型
     */
    private List<FloorDetailDto> houseList;

}
