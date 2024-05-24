package cn.cuiot.dmp.archive.application.param.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author liujianyu
 * @description 导入房屋档案
 * @since 2024-05-16 11:07
 */
@Data
public class HousesArchiveImportDto implements Serializable {

    @Excel(name = "房屋名称", orderNum = "0")
    private String name;

    @Excel(name = "房号", orderNum = "1")
    private String roomNum;

    @Excel(name = "房屋编码", orderNum = "2")
    private String code;
//
//    /**
//     * 房屋楼层
//     */
//    @Excel(name = "房屋楼层", orderNum = "3")
//    private String floorName;
//
//    /**
//     * 楼层别名
//     */
//    @Excel(name = "", orderNum = "")
//    private String floorAlias;
//
//    /**
//     * 容量
//     */
//    @Excel(name = "", orderNum = "")
//    private String capacity;
//
//    /**
//     * 期别
//     */
//    @Excel(name = "", orderNum = "")
//    private String qibie;
//
//    /**
//     * 楼层系数
//     */
//    @Excel(name = "", orderNum = "")
//    private String floorCoefficient;
//
//    /**
//     * 使用面积（支持4位小数，最长可输入15位）
//     */
//    @Excel(name = "", orderNum = "")
//    private Double usableArea;
//
//    /**
//     * 收费面积（支持4位小数，最长可输入15位）
//     */
//    @Excel(name = "", orderNum = "")
//    private Double chargeArea;
//
//    /**
//     * 公摊面积（支持4位小数，最长可输入15位）
//     */
//    @Excel(name = "", orderNum = "")
//    private Double sharedArea;
//
//    /**
//     * 使用率（自动计算）
//     */
//    @Excel(name = "", orderNum = "")
//    private Double utilizationRate;
//
//    /**
//     * 产权单位（支持输入汉字、英文、符号、数字，长度支持50字符）
//     */
//    @Excel(name = "", orderNum = "")
//    private String ownershipUnit;
//
//    /**
//     * 交房时间（年月日）
//     */
//    @Excel(name = "", orderNum = "")
//    private LocalDate handoverDate;
//
//    /**
//     * 责任人（支持输入汉字、英文、符号、数字，长度支持10字符）
//     */
//    @Excel(name = "", orderNum = "")
//    private String responsiblePerson;
//
//    /**
//     * 请填写备注（支持输入汉字、英文、符号、数字，长度支持200字符）
//     */
//    @Excel(name = "", orderNum = "")
//    private String remarks;
//
//    /**
//     * 房源标题（支持输入汉字、英文、符号、数字，长度支持30字符）
//     */
//    @Excel(name = "", orderNum = "")
//    private String listingTitle;
//
//    /**
//     * 房源描述（支持输入汉字、英文、符号、数字，长度支持500字符）
//     */
//    @Excel(name = "", orderNum = "")
//    private String listingDescription;
}
