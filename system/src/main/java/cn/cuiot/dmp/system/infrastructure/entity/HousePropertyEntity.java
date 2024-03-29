package cn.cuiot.dmp.system.infrastructure.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物业侧房屋
 * @author huw51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HousePropertyEntity {

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
     * 园区id
     */
    private Long parkId;

    /**
     * 楼栋
     */
    private Long buildingId;

    /**
     * 区域Id
     */
    private Long regionId;

    /**
     * 楼层id
     */
    private Long floorId;

    /**
     * 房屋名称
     */
    private String houseName;

    /**
     * 房号
     */
    private String houseNum;

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
     * 使用状态
     */
    private Integer usedStatus;
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

    /**
     * 房屋属性
     */
    private String houseAttribute;
}
