package cn.cuiot.dmp.system.infrastructure.entity.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小区楼栋房屋信息
 *
 * @author lixf
 */
@NoArgsConstructor
@Data
public class SpaceFloorDetailResDto {

    /**
     * 小区类型
     */
    private String type;
    /**
     * 楼层房屋类型
     */
    private List<SpaceFloorDetailDto> floorList = new ArrayList<>();

}
