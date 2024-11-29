package cn.cuiot.dmp.archive.application.param.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.cuiot.dmp.base.application.dto.BaseExcelModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/21
 */
@Data
public class BuildingArchiveImportDTO extends BaseExcelModel implements Serializable {

    private static final long serialVersionUID = -1323217558185448570L;

    /**
     * 楼盘名称
     */
    @Excel(name = "楼盘名称")
    @NotBlank(message = "楼盘名称不能为空")
    private String name;

    /**
     * 楼盘类型
     */
    @Excel(name = "楼盘类型")
    @NotBlank
    private String type;

    /**
     * 详细地址
     */
    @Excel(name = "楼盘详细地址", orderNum = "1")
    @NotBlank
    private String areaDetail;

    /**
     * 楼栋数
     */
    @Excel(name = "楼栋数", orderNum = "2")
    @NotNull(message = "不能为空")
    private Integer buildingNum;

    /**
     * 房屋数
     */
    @Excel(name = "房屋数", orderNum = "3")
    private Integer houseNum;

    /**
     * 车位数
     */
    @Excel(name = "车位数", orderNum = "4")
    private Integer parkNum;

    /**
     * 网格员联系方式
     */
    @Excel(name = "网格员联系方式", orderNum = "5")
    private String staffPhone;

}
