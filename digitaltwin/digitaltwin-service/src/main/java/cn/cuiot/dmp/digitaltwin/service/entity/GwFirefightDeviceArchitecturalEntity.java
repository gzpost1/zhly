package cn.cuiot.dmp.digitaltwin.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 格物消防-建筑对象
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@Data
@TableName("tb_gw_firefight_device_architectural")
public class GwFirefightDeviceArchitecturalEntity {
    /**
     * 父级id(对应设备id)
     */
    private Long parentId;

    /**
     * 建筑id
     */
    private String architecturalId;

    /**
     * 建筑名称
     */
    private String name;

    /**
     * 建筑类型
     */
    private String type;

    /**
     * 建筑类型名 称
     */
    private String typeName;

    /**
     * 建筑层数 （地上）
     */
    private String storeyUp;

    /**
     * 建筑层数 （地下）
     */
    private String storeyDown;

    /**
     * 所属组织
     */
    private String deptId;

    /**
     * 组织名称
     */
    private String departmentName;

    /**
     * 消防安全管 理人
     */
    private String safetyManager;

    /**
     * 消防安全管 理人电话
     */
    private String safetyManagerPhone;

    /**
     * 消防安全责 任人
     */
    private String dutyManager;

    /**
     * 消防安全责 任人电话
     */
    private String dutyManagerPhone;

    /**
     * 物联单位
     */
    private String iotUnit;

    /**
     * 建筑楼层高 度
     */
    private String buildingHeight;

    /**
     * 建筑结构
     */
    private String buildingStructure;

    /**
     * 有无整改记 录
     */
    private String rectificationRecord;

    /**
     * 耐火等级
     */
    private String fireResistanceRating;

    /**
     * 危险等级
     */
    private String dangerLevel;

    /**
     * 消控室
     */
    private String roomId;

    /**
     * 消控室位置
     */
    private String roomLocation;

    /**
     * 建筑使用情 况
     */
    private String serviceCondition;

    /**
     * 建筑用途
     */
    private String constructionApplications;

    /**
     * 产权情况
     */
    private String propertyRightSituation;

    /**
     * 重点说明
     */
    private String mainlyIllustrate;

    /**
     * 标准层面积
     */
    private String markerBedArea;

    /**
     * 地上面积
     */
    private String groundArea;

    /**
     * 地下面积
     */
    private String ungroundArea;

    /**
     * 最大容纳人 数
     */
    private String galleryful;

    /**
     * 避难层数量
     */
    private String refugeStoreyCount;

    /**
     * 避难层位置
     */
    private String refugeStoreyLocation;

    /**
     * 避难层面积
     */
    private String refugeStoreyArea;

    /**
     * 消防电梯数
     */
    private String fireLiftCount;

    /**
     * 消防电梯位 置
     */
    private String fireLiftLocation;

    /**
     * 消防系统状 态
     */
    private String fireSystemState;

    /**
     * 消防设计审 核日期
     */
    private String checkTime;

    /**
     * 消防验收日 期
     */
    private String acceptTime;

    /**
     * 省市区县
     */
    private String areaCode;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 维度
     */
    private String latitude;

    /**
     * 房间数
     */
    private String roomCount;

    /**
     * 单位数量
     */
    private String unitCount;

    /**
     * 行政区划
     */
    private String areaAllName;

    /**
     * 消控室名称
     */
    private String roomName;

    /**
     * 创建时间
     */
    private String createTime;
}
