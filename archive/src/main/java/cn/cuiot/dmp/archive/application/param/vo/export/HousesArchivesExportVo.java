package cn.cuiot.dmp.archive.application.param.vo.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

@Data
public class HousesArchivesExportVo implements Serializable {

    /**
     * id
     */
    @Excel(name = "房屋ID", width = 30)
    private Long id;

    /**
     * 房屋编码
     */
    @Excel(name = "房屋编码", orderNum = "1", width = 30)
    private String code;
    /**
     * 房号
     */
    @Excel(name = "房号", orderNum = "2", width = 30)
    private String roomNum;

    /**
     * 房屋楼层
     */
    @Excel(name = "房屋楼层", orderNum = "12", width = 30)
    private String floorName;

    /**
     * 楼层别名
     */
    @Excel(name = "楼层别名", orderNum = "13", width = 30)
    private String floorAlias;

    /**
     * 容量
     */
    @Excel(name = "容量", orderNum = "10", width = 30)
    private String capacity;

    /**
     * 期别
     */
    @Excel(name = "期别", orderNum = "11", width = 30)
    private String qibie;

    /**
     * 房屋户型
     */
    @Excel(name = "房屋户型", orderNum = "8", width = 30)
    private Long houseType;

    /**
     * 建筑面积（支持4位小数，最长可输入15位）
     */
    @Excel(name = "建筑面积", orderNum = "4", width = 30)
    private Double buildingArea;

    /**
     * 使用面积（支持4位小数，最长可输入15位）
     */
    @Excel(name = "使用面积", orderNum = "6", width = 30)
    private Double usableArea;

    /**
     * 收费面积（支持4位小数，最长可输入15位）
     */
    @Excel(name = "收费面积", orderNum = "5", width = 30)
    private Double chargeArea;

    /**
     * 公摊面积（支持4位小数，最长可输入15位）
     */
    @Excel(name = "公摊面积", orderNum = "9", width = 30)
    private Double sharedArea;

    /**
     * 物业业态
     */
    @Excel(name = "物业业态", orderNum = "14", width = 30)
    private Long propertyType;

    /**
     * 状态
     */
    @Excel(name = "状态", orderNum = "7", width = 30, replace = {"启用_1", "停用_0"})
    private String status;

}