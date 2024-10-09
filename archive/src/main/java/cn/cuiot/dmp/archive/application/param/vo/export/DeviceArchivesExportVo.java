package cn.cuiot.dmp.archive.application.param.vo.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 设备档案导出
 * </p>
 *
 * @author hantingyao
 * @since 2024-05-15
 */
@Data
public class DeviceArchivesExportVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Excel(name = "设备编码", width = 20, orderNum = "1")
    private Long id;

    /**
     * 设备名称（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    @Excel(name = "设备名称", width = 30, orderNum = "2")
    private String deviceName;

    /**
     * 设备类别（下拉选择自定义配置中数据）
     */
    @Excel(name = "设备类别", width = 30, orderNum = "4")
    private String deviceCategoryName;

    /**
     * 设备系统（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    @Excel(name = "设备系统", width = 30, orderNum = "3")
    private String deviceSystem;

    /**
     * 设备专业（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    @Excel(name = "设备专业", width = 30, orderNum = "5")
    private String deviceProfessional;

    /**
     * 安装位置（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    @Excel(name = "安装位置", width = 30, orderNum = "6")
    private String installationLocation;

//    /**
//     * 设备状态（具体选项）
//     */
//    private Long deviceStatus;

    /**
     * 设备状态（具体选项）
     */
    @Excel(name = "设备状态", width = 30, orderNum = "7")
    private String deviceStatusName;


}
