package cn.cuiot.dmp.archive.application.param.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liujianyu
 * @description 设备档案导出vo
 * @since 2024-05-16 10:06
 */
@Data
public class DeviceArchivesExportVo implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * id就是设备编码
     */
    @Excel(name = "设备编码", orderNum = "0", width = 20)
    private String idStr;

    /**
     * 设备名称（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    @Excel(name = "设备名称", orderNum = "1", width = 20)
    private String deviceName;

    /**
     * 设备系统（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    @Excel(name = "设备系统", orderNum = "2", width = 20)
    private String deviceSystem;

    /**
     * 设备类别（下拉选择自定义配置中数据）-id转换为名称
     */
    @Excel(name = "设备类别", orderNum = "3", width = 20)
    private String deviceCategoryName;

    /**
     * 设备专业（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    @Excel(name = "设备专业", orderNum = "4", width = 20)
    private String deviceProfessional;

    /**
     * 安装位置（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    @Excel(name = "安装位置", orderNum = "5", width = 20)
    private String installationLocation;

    /**
     * 设备状态（下拉选择自定义配置中数据）-id转换为名称
     */
    @Excel(name = "设备状态", orderNum = "6", width = 20)
    private String deviceStatusString;
}
