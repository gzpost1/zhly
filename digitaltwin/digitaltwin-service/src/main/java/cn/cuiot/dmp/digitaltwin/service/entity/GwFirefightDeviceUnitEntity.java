package cn.cuiot.dmp.digitaltwin.service.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 格物消防-单位信息
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
@Data
@TableName("tb_gw_firefight_device_unit")
public class GwFirefightDeviceUnitEntity {
    /**
     * 父级id(对应设备id)
     */
    private Long parentId;

    /**
     * 单位ID
     */
    private String unitId;

    /**
     * 单位名称
     */
    private String name;

    /**
     * 统一社会信用代码
     */
    private String socialCredit;

    /**
     * 所属行业
     */
    private String industryInvolved;

    /**
     * 所属行业名称
     */
    private String industryInvolvedName;

    /**
     * 单位类型
     */
    private String type;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 所属组织 id
     */
    private String deptId;

    /**
     * 组织名称
     */
    private String departmentName;

    /**
     * 消防安全管理人
     */
    private String safetyManager;

    /**
     * 消防安全管理人电话
     */
    private String safetyManagerPhone;

    /**
     * 物业管理员
     */
    private String propertyManager;

    /**
     * 物业管理员电话
     */
    private String propertyManagerPhone;

    /**
     * 单位电话
     */
    private String unitPhone;

    /**
     * 监管类别
     */
    private String regulatoryCategory;

    /**
     * 监管类别名称
     */
    private String regulatoryCategoryName;

    /**
     * 属地管辖部门
     */
    private String administerDepartment;

    /**
     * 行业主管部门
     */
    private String industryDepartment;

    /**
     * 街道专职救援管辖部门
     */
    private String streetDepartment;

    /**
     * 执法管辖部门
     */
    private String lawDepartment;

    /**
     * 维保单位
     */
    private String maintenanceUnit;

    /**
     * 竣工时间
     */
    private String completionTime;

    /**
     * 省市区县街道
     */
    private String areaCode;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 维度
     */
    private String latitude;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 消防安全责任人
     */
    private String dutyManager;

    /**
     * 消防安全责任人电话
     */
    private String dutyManagerPhone;

    /**
     * 消防管理机
     */
    private String fireManagementAgency;

    /**
     * 救援线
     */
    private String rescueLine;

    /**
     * 建造时间
     */
    private String constructionTime;

    /**
     * 监管级别
     */
    private String supervisionLevel;

    /**
     * 监管级别名称
     */
    private String supervisionLevelName;

    /**
     * 单位级别
     */
    private String unitLevel;

    /**
     * 单位级别名称
     */
    private String unitLevelName;

    /**
     * 单位性质
     */
    private String unitNature;

    /**
     * 单位性质名称
     */
    private String unitNatureName;

    /**
     * 单位人数
     */
    private String unitCount;

    /**
     * 单位面积
     */
    private String unitArea;

    /**
     * 创建时间
     */
    private String createTime;
}
