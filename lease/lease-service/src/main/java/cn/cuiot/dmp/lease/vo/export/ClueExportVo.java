package cn.cuiot.dmp.lease.vo.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author hantingyao
 * @Description
 * @data 2024/9/30 9:19
 */
@Data
public class ClueExportVo {

    /**
     * 线索名称
     */
    @Excel(name = "线索名称", width = 20,orderNum = "1")
    private String name;

    /**
     * 线索ID
     */
    @Excel(name = "线索ID", width = 20,orderNum = "2")
    private Long id;

    /**
     * 楼盘名称
     */
    @Excel(name = "楼盘名称", width = 20,orderNum = "3")
    private String buildingName;

    /**
     * 线索来源名称
     */
    @Excel(name = "线索来源名称", width = 20,orderNum = "4")
    private String sourceIdName;

    /**
     * 创建者名称
     */
    @Excel(name = "创建者名称", width = 20,orderNum = "5")
    private String createdName;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 20,orderNum = "6",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
}
