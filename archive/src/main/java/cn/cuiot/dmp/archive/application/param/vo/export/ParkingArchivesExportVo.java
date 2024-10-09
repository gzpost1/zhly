package cn.cuiot.dmp.archive.application.param.vo.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 车位档案
 * </p>
 *
 * @author hantingyao
 * @since 2024-05-15
 */
@Data
public class ParkingArchivesExportVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车位编号
     */
    @Excel(name = "车位编号", width = 30, orderNum = "2")
    private String code;

    /**
     * 所属区域（下拉选择自定义配置中数据）
     */
    @Excel(name = "所属区域", width = 30, orderNum = "1")
    private String areaName;


    /**
     * 使用情况（下拉选择自定义配置中数据）
     */
    @Excel(name = "使用情况", width = 30, orderNum = "3")
    private String usageStatusName;

    /**
     * 状态
     */
    @Excel(name = "状态", width = 30, orderNum = "6", replace = {"1_启用", "0_停用"})
    private String status;

    /**
     * 图片（支持png/jpg/jpeg，小于50M，最多上传1张，点击【添加】按钮，调用系统【打开】，单选上传文件）
     */
    @Excel(name = "图片", width = 30, orderNum = "5")
    private String image;

    /**
     * 车位类型（下拉选择自定义配置中数据）
     */
    @Excel(name = "车位类型", width = 30, orderNum = "4")
    private String parkingTypeName;

    /**
     * 备注（支持输入汉字、数字、符号，最长长度为100位字符）
     */
    @Excel(name = "备注", width = 30, orderNum = "7")
    private String remarks;

}
