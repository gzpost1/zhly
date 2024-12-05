package cn.cuiot.dmp.archive.application.param.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liujianyu
 * @description 房屋档案导出用vo
 * @since 2024-05-16 10:06
 */
@Data
public class HousesArchiveExportVo implements Serializable {

    private static final long serialVersionUID = -1L;
    /**
     * 所属组织
     */
    @Excel(name = "所属组织", orderNum = "0", width = 20)
    private String deptName;

    /**
     * 所属楼盘名称-需从id转换
     */
    @Excel(name = "所属楼盘", orderNum = "0", width = 20)
    private String loupanName;
    /**
     * 房屋编码
     */
    @Excel(name = "房屋编码", orderNum = "0", width = 20)
    private String code;



    /**
     * 房号
     */
    @Excel(name = "房号", orderNum = "2", width = 20)
    private String roomNum;

    @Excel(name = "房屋ID", orderNum = "3", width = 20)
    private Long id;

    /**
     * 建筑面积（支持4位小数，最长可输入15位）
     */
    @Excel(name = "建筑面积", orderNum = "4", width = 20)
    private String buildingArea;

    /**
     * 使用面积（支持4位小数，最长可输入15位）
     */
    @Excel(name = "使用面积", orderNum = "5", width = 20)
    private String usableArea;

    /**
     * 收费面积（支持4位小数，最长可输入15位）
     */
    @Excel(name = "收费面积", orderNum = "6", width = 20)
    private String chargeArea;

    /**
     * 状态-需从id转换
     */
    @Excel(name = "状态", orderNum = "7", width = 20)
    private String statusName;

    /**
     * 房屋户型-需从id转换
     */
    @Excel(name = "房屋户型", orderNum = "8", width = 20)
    private String houseTypeName;

    /**
     * 容量
     */
    @Excel(name = "容量", orderNum = "9", width = 20)
    private String capacity;

    /**
     * 期别
     */
    @Excel(name = "期别", orderNum = "10", width = 20)
    private String qibie;

    /**
     * 房屋楼层
     */
    @Excel(name = "房屋楼层", orderNum = "11", width = 20)
    private String floorName;

    /**
     * 楼层别名
     */
    @Excel(name = "楼层别名", orderNum = "12", width = 20)
    private String floorAlias;

    /**
     * 物业业态-需从id转换
     */
    @Excel(name = "物业业态", orderNum = "13", width = 20)
    private String propertyTypeName;
}
