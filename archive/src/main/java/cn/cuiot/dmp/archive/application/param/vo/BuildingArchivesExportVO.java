package cn.cuiot.dmp.archive.application.param.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/21
 */
@Data
public class BuildingArchivesExportVO implements Serializable {

    private static final long serialVersionUID = -2786390850264667355L;

    /**
     * 楼盘编码
     */
    @Excel(name = "楼盘编码", width = 20)
    private String id;

    /**
     * 楼盘名称
     */
    @Excel(name = "楼盘名称", orderNum = "1", width = 20)
    private String name;

    /**
     * 楼盘地址
     */
    @Excel(name = "楼盘地址", orderNum = "2", width = 20)
    private String areaName;

    /**
     * 所属组织
     */
    @Excel(name = "所属组织", orderNum = "3", width = 20)
    private String departmentName;

    /**
     * 楼栋数
     */
    @Excel(name = "楼栋数", orderNum = "4", width = 20)
    private Integer buildingNum;

    /**
     * 房屋数
     */
    @Excel(name = "房屋数", orderNum = "5", width = 20)
    private Integer houseNum;

    /**
     * 车位数
     */
    @Excel(name = "车位数", orderNum = "6", width = 20)
    private Integer parkNum;

    /**
     * 网格员联系方式
     */
    @Excel(name = "网格员联系方式", orderNum = "7", width = 20)
    private String staffPhone;

}
