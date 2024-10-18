package cn.cuiot.dmp.largescreen.service.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/21
 */
@Data
public class BuildingArchivesVO implements Serializable {

    private static final long serialVersionUID = 7765681889467534712L;

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
     * 企业ID
     */
    private Long companyId;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 部门名称(路径)
     */
    private String departmentName;

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

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 档案二维码id
     */
    private Long qrCodeId;

}
