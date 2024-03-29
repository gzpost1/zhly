package cn.cuiot.dmp.system.infrastructure.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author xieSH
 * @Description 房屋entity
 * @Date 2021/12/27 9:31
 * @param
 * @return
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseEntity {

    /**
     * 主键
     */
    private Long id;
    /**
     * 租户主键id
     */
    private Long pkOrgId;

    /**
     * 传递参数用
     */
    private Long deptId;

    /**
     * 组织id(自身)
     */
    private Long selfDeptId;
    /**
     * 小区id
     */
    private Long communityId;
    /**
     * 楼栋
     */
    private Long buildingId;

    /**
     * 房号
     */
    private String name;
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
     * 房屋面积
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
     * 储藏号
     */
    private String storageNo;
    /**
     * 车位编号
     */
    private String parkingNo;
    /**
     * 房屋朝向
     */
    private String orientation;
    /**
     * 产权年限(70、40、20、10、临时)
     */
    private Integer propertyYears;
    /**
     * 楼层数
     */
    private Integer floorNum;
    /**
     * 房屋类型，1住宅 2商铺 3办公
     */
    private Integer houseType;
    /**
     * 房屋状态，1常住户 2非常住户 3装修户 4未交房 5未售
     */
    private Integer houseStatus;
    /**
     * 使用状态，1自用 2已租 3空置 4待租 5待售 6已售 7其它
     */
    private Integer usedStatus;
    /**
     * 交房时间
     */
    private LocalDateTime deliveryTime;
    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createdOn;
    /**
     * 创建者id
     */
    private String createdBy;
    /**
     * 更新时间
     */
    private LocalDateTime updatedOn;
    /**
     * 更新者id
     */
    private String updatedBy;
    /**
     * 是否删除（0：未删除 1：已删除）
     */
    private Integer deletedFlag;
    /**
     * 删除时间
     */
    private LocalDateTime deletedOn;
    /**
     * 删除人id
     */
    private String deletedBy;
}
