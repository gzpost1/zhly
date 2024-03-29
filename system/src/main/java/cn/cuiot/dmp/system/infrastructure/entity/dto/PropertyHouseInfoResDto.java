package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * @author huw51
 */
@Data
public class PropertyHouseInfoResDto {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 房屋id
     */
    private Long houseId;

    /**
     * 组织id
     */
    private Long pkOrgId;

    /**
     * 组织id
     */
    private Long deptId;


    /**
     * 组织名称
     */
    private String deptName;

    /**
     * 园区id
     */
    private Long parkId;

    /**
     * 园区编号
     */
    private String parkCode;

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
    private Long regionId;

    /**
     * 区域名称
     */
    private String regionName;


    /**
     * 楼栋层数
     */
    private String buildingFloor;

    /**
     * 描述
     */
    private String description;

    /**
     * 空间树
     */
    private String spaceTreePath;

    /**
     * 组织树
     */
    private String deptTreePath;

    /**
     * 使用状态
     */
    private String usedStatus;

}
