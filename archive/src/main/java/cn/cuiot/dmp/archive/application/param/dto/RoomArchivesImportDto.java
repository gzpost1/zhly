package cn.cuiot.dmp.archive.application.param.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liujianyu
 * @description 导入空间档案
 * @since 2024-05-16 11:07
 */
@Data
public class RoomArchivesImportDto implements Serializable {

    /**
     * 空间名称（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    @Excel(name = "空间名称", orderNum = "0")
    private String name;

    /**
     * 空间分类（下拉选择自定义配置中数据）
     */
    @Excel(name = "空间分类", orderNum = "1")
    private String spaceCategoryName;

    /**
     * 专业用途（下拉选择自定义配置中数据）
     */
    @Excel(name = "专业用途", orderNum = "2")
    private String professionalPurposeName;

    /**
     * 定位偏差（支持输入汉字、数字、符号，最长长度为3位数字）
     */
    @Excel(name = "定位偏差", orderNum = "3")
    private String locationDeviation;
}
