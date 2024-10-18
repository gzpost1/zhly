package cn.cuiot.dmp.archive.domain.aggregate;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/20
 */
@Data
public class BuildingArchives implements Serializable {

    private static final long serialVersionUID = 1628560153990448430L;

    /**
     * id
     */
    private Long id;

    /**
     * 楼盘名称
     */
    private String name;

    /**
     * 区域编码
     */
    private String areaCode;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 详细地址
     */
    private String areaDetail;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 详细位置
     */
    private String address;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 楼栋数
     */
    private Integer buildingNum;

    /**
     * 房屋数
     */
    private Integer houseNum;

    /**
     * 车位数
     */
    private Integer parkNum;

    /**
     * 网格员联系方式
     */
    private String staffPhone;

    /**
     * 停启用状态（0停用，1启用）
     */
    private Byte status;

}
