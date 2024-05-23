package cn.cuiot.dmp.archive.application.param.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liujianyu
 * @description 导入车位档案
 * @since 2024-05-16 11:07
 */
@Data
public class ParkingArchivesImportDto implements Serializable {

    /**
     * 所属区域（下拉选择自定义配置中数据）-名称需要转id
     */
    @Excel(name = "所属区域", orderNum = "0")
    private String areaName;

    /**
     * 车位编号
     */
    @Excel(name = "车位编号", orderNum = "1")
    private String code;

    /**
     * 使用情况（下拉选择自定义配置中数据）-名称需要转id
     */
    @Excel(name = "使用情况", orderNum = "2")
    private String usageStatusName;

    /**
     * 车位类型（下拉选择自定义配置中数据）-名称需要转id
     */
    @Excel(name = "车位类型", orderNum = "3")
    private String parkingTypeName;

    /**
     * 状态
     */
    @Excel(name = "状态", orderNum = "4")
    private String statusName;

    @Excel(name = "楼盘名称", orderNum = "5")
    private String loupanName;

}
