package cn.cuiot.dmp.archive.application.param.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liujianyu
 * @description 车位档案导出
 * @since 2024-05-16 10:06
 */
@Data
public class ParkingArchivesExportVo implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 车位编号
     */
    @Excel(name = "车位编号", orderNum = "0", width = 20)
    private String code;
    /**
     * 所属区域（下拉选择自定义配置中数据）-需从id转换
     */
    @Excel(name = "所属区域", orderNum = "1", width = 20)
    private String areaName;

    /**
     * 车位类型（下拉选择自定义配置中数据）-需从id转换
     */
    @Excel(name = "车位类型", orderNum = "2", width = 20)
    private String parkingTypeName;

    /**
     * 状态-需从id转换
     */
    @Excel(name = "状态", orderNum = "3", width = 20)
    private String statusName;

    /**
     * 使用情况（下拉选择自定义配置中数据）-需从id转换
     */
    @Excel(name = "使用情况", orderNum = "4", width = 20)
    private String usageStatusName;

    /**
     * 备注（支持输入汉字、数字、符号，最长长度为100位字符）
     */
    @Excel(name = "备注", orderNum = "5", width = 20)
    private String remarks;
}
