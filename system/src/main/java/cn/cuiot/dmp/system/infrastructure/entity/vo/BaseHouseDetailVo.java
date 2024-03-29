package cn.cuiot.dmp.system.infrastructure.entity.vo;

import lombok.Data;

/**
 * @author huw51
 */
@Data
public class BaseHouseDetailVo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 组织id
     */
    private Long deptId;

    /**
     * 楼栋id
     */
    private Long buildingId;

    /**
     * 楼栋名称
     */
    private String buildingName;

    /**
     * 楼层id
     */
    private Long floorId;

    /**
     * 楼层
     */
    private String floorName;

    /**
     * 当前楼层
     */
    private String currentFloor;

    /**
     * 楼栋层数
     */
    private String buildingFloor;

    /**
     * 商铺自身id
     */
    private Long houseId;

    /**
     * 房屋，商铺名称
     */
    private String houseName;

    /**
     * 房号
     */
    private String houseNum;

    /**
     * 商铺面积
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
     * 描述，备注
     */
    private String description;

    /**
     * 组织树路径
     */
    private String deptTreePath;

}
