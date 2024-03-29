package cn.cuiot.dmp.system.infrastructure.entity.po;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 房屋基类PO
 * @author huwei
 */
@Data
public class HousePo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 组织id
     */
    private Long deptId;

    /**
     * 商业综合体，园区，小区id
     */
    private Long communityId;

    /**
     * 商业综合体，园区，小区名称
     */
    private String communityName;

    /**
     * 商业综合体，园区，小区编号
     */
    private String communityCode;

    /**
     * 区域id
     */
    private Long regionId;

    /**
     * 区域名称
     */
    private String regionName;

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
    private Long selfDeptId;

    /**
     * 房屋，商铺名称
     */
    private String houseName;

    /**
     * 房号
     */
    private String name;

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
     * 组织路径
     */
    private String deptTreePath;

    /**
     * 房屋数
     */
    private String room;

    /**
     * 厅数
     */
    private String hall;

    /**
     * 卫浴
     */
    private String bathroom;

    /**
     * 车位编号
     */
    private String parkingNo;

    /**
     * 储物间号
     */
    private String storageNo;

    /**
     * 房屋朝向
     */
    private String orientation;

    /**
     * 交房时间
     */
    private LocalDateTime deliveryTime;

    /**
     * 房屋类型
     */
    private String houseType;

    /**
     * 房屋状态
     */
    private Integer houseStatus;

    /**
     * 使用状态
     */
    private Integer usedStatus;

    /**
     * 使用年限
     */
    private String propertyYears;

    /**
     * 楼层
     */
    private Integer floorNum;

    /**
     * 场所名称
     */
    private String spaceName;

    /**
     * 费用期间 开始时间
     */
    private String startTime;

    /**
     * 费用期间 结束时间
     */
    private String endTime;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 手机号码
     */
    private String phoneNumber;

}
