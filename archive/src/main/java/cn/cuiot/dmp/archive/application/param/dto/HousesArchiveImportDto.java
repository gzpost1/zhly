package cn.cuiot.dmp.archive.application.param.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

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

    @Excel(name = "楼盘名称", orderNum = "2")
    private String loupanName;

    @Excel(name = "房屋编码", orderNum = "3")
    private String code;
}
