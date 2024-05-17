package cn.cuiot.dmp.archive.application.param.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author liujianyu
 * @description 导入设备档案
 * @since 2024-05-16 11:07
 */
@Data
public class DeviceArchivesImportDto implements Serializable {

    /**
     * 设备名称（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    @Excel(name = "设备名称", orderNum = "0")
    private String deviceName;

    /**
     * 设备类别（下拉选择自定义配置中数据）-名称需要转id
     */
    @Excel(name = "设备类别", orderNum = "1")
    private String deviceCategoryName;

    /**
     * 安装位置（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    @Excel(name = "安装位置", orderNum = "2")
    private String installationLocation;

    /**
     * 安装日期（支持选择年月日）
     */
    @Excel(name = "安装日期", orderNum = "3")
    private String installationDateName;

}
