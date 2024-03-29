package cn.cuiot.dmp.system.infrastructure.entity.vo;

import cn.hutool.core.annotation.Alias;
import java.time.LocalDateTime;
import lombok.Data;


/**
 * @author cwl
 * @classname HouseListVO
 * @description 房屋列表查询入参
 * @date 2021/12/28
 */
@Data
public class HouseListVO {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 组织id
     */
    private Long selfDeptId;

    /**
     * 小区id
     */
    private Long communityId;

    /**
     * 小区名称
     */
    private String communityName;

    /**
     * 楼栋id
     */
    private Long buildingId;

    /**
     * 楼栋名称
     */
    private String buildingName;

    /**
     * 储藏号
     */
    private String storageNo;

    /**
     * 车位编号
     */
    private String parkingNo;

    /**
     * 房号
     */
    private String name;

    /**
     * 楼层
     */
    @Alias(value = "楼层")
    private Integer floorNum;

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
     * 室
     */
    private Integer room;

    /**
     * 厅
     */
    private Integer hall;

    /**
     * 卫
     */
    private Integer bathroom;

    /**
     * 交房时间
     */
    private LocalDateTime deliveryTime;

    /**
     * 房屋类型，1住宅 2商铺 3办公
     */
    private Integer houseType;

    /**
     * 房屋状态，1常住户 2非常住户 3装修户 4未交房 5未售
     */
    private Integer houseStatus;

    /**
     * 使用状态，1自用2已租3空置4待租5待售6已售7其它
     */
    private Integer usedStatus;

    /**
     * 房屋朝向
     */
    private String orientation;

    /**
     * 描述
     */
    private String description;

    /**
     * 组织树
     */
    private String path;

    /**
     * 场所名称
     */
    private String spaceName;

}
