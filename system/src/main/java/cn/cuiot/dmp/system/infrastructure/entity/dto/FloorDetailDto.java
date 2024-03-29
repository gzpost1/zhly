package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 空间楼层详情dto
 *
 * @author lixf
 */
@NoArgsConstructor
@Data
public class FloorDetailDto {

    /**
     * 房号
     */
    private String name;
    /**
     * 房屋面积
     */
    private String houseArea;
    /**
     * 使用状态
     */
    private Integer usedStatus;
    /**
     * 房屋属性
     */
    private String houseAttribute;
    /**
     * 房屋类型
     */
    private String houseType;
    /**
     * 楼层
     */
    @JsonIgnore
    private Integer floor;
    /**
     * path
     */
    @JsonIgnore
    private String path;
    
}
