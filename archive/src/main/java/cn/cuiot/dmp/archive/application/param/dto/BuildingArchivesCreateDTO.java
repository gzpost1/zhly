package cn.cuiot.dmp.archive.application.param.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/21
 */
@Data
public class BuildingArchivesCreateDTO implements Serializable {

    private static final long serialVersionUID = -5383740074465289894L;

    /**
     * 楼盘名称
     */
    @NotBlank(message = "楼盘名称不能为空")
    private String name;

    /**
     * 区域编码
     */
    @NotBlank(message = "区域编码不能为空")
    private String areaCode;

    /**
     * 区域名称
     */
    @NotBlank(message = "区域名称不能为空")
    private String areaName;

    /**
     * 详细地址
     */
    @NotBlank(message = "详细地址不能为空")
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
     * 部门ID
     */
    @NotNull(message = "部门ID不能为空")
    private Long departmentId;

    /**
     * 楼栋数
     */
    @NotNull(message = "楼栋数不能为空")
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

}
