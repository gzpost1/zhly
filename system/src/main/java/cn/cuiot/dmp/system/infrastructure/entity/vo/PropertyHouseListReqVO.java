package cn.cuiot.dmp.system.infrastructure.entity.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;


/**
 * @author huw51
 * @classname CommercialHouseListVO
 * @description 房屋列表查询入参
 * @date 2023/01/11
 */
@Data
public class PropertyHouseListReqVO {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 园区id
     */
    private Long parkId;

    /**
     * 小区名称
     */
    private String parkName;

    /**
     * 楼栋id
     */
    private Long buildingId;

    /**
     * 楼层id
     */
    private Long floorId;

    /**
     * 楼栋名称
     */
    private String buildingName;

    /**
     * 房间名称
     */
    private String houseName;

    /**
     * 房号
     */
    private String houseNum;

    /**
     * 楼层
     */
    private String floorName;

    /**
     * 当前楼层
     */
    private String currentFloor;

    /**
     * 建筑面积
     */
    private String houseArea;

    /**
     * 使用面积
     */
    private String usedArea;

    /**
     * 公摊面积
     */
    private String publicArea;

    /**
     * 房屋属性
     */
    private String houseAttribute;

    /**
     * 区域id
     */
    private String regionId;

    /**
     * 区域名称
     */
    private String regionName;


    /**
     * 楼栋层数
     */
    private String buildingFloor;

    /**
     * 描述，备注
     */
    @Length(max = 200, message = "房屋描述不可超过200字")
    private String description;

    /**
     * 房屋自身id
     */
    private String houseId;

    /**
     * 使用状态（2：已租，4：待租）
     */
    private Integer usedStatus;

    /**
     * 场所名称
     */
    private String spaceName;


}
