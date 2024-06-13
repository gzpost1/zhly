package cn.cuiot.dmp.base.infrastructure.model;//	模板

import lombok.Data;

import java.io.Serializable;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/30 16:47
 */
@Data
public class BuildingArchive implements Serializable {

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
