package cn.cuiot.dmp.archive.application.param.vo.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 空间档案
 * </p>
 *
 * @author hantingyao
 * @since 2024-05-15
 */
@Data
public class RoomArchivesExportVo implements Serializable {

    /**
     * 空间名称（支持输入汉字、英文、符号、数字，长度支持30字符）
     */
    @Excel(name = "空间名称", orderNum = "1", width = 30)
    private String name;

    /**
     * 空间分类（下拉选择自定义配置中数据）
     */
    @Excel(name = "空间分类", orderNum = "2", width = 30)
    private String spaceCategoryName;

    /**
     * 详细位置（详细地址支持输入汉字、英文、符号、数字，长度支持50字符）
     */
    @Excel(name = "详细位置", orderNum = "3", width = 30)
    private String location;

    /**
     * 专业用途（下拉选择自定义配置中数据）
     */
    @Excel(name = "专业用途", orderNum = "4", width = 30)
    private String professionalPurposeName;

    /**
     * 空间面积（支持输入数字，最多支持输入7位数字）
     */
    @Excel(name = "空间面积", orderNum = "5", width = 30)
    private Long spaceArea;

    /**
     * 经营性质（下拉选择自定义配置中数据）
     */
    @Excel(name = "经营性质", orderNum = "6", width = 30)
    private String businessNatureName;

    /**
     * 产权单位（支持输入汉字、数字、符号，最长长度为50位数字）
     */
    @Excel(name = "产权单位", orderNum = "7", width = 30)
    private String ownershipUnit;


    /**
     * 产权属性（下拉选择自定义配置中数据）
     */
    @Excel(name = "产权属性", orderNum = "8", width = 30)
    private String ownershipAttributeName;

    /**
     * 资源类型（下拉选项）
     */
    @Excel(name = "资源类型", orderNum = "9", width = 30)
    private String resourceTypeName;

    /**
     * 定位方式（下拉选项）
     */
    @Excel(name = "定位方式", orderNum = "10", width = 30)
    private String locationMethodName;


    /**
     * 状态（默认启用，点击关闭）
     */
    @Excel(name = "状态", orderNum = "11", width = 30, replace = {"启用_1", "停用_0"})
    private Byte status;

}