package cn.cuiot.dmp.lease.vo.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author hantingyao
 * @Description
 * @data 2024/9/30 9:51
 */
@Data
public class ClueFollowUpExportVo extends ClueExportVo{

    /**
     * 跟进状态名称
     */
    @Excel(name = "跟进状态", width = 20,orderNum = "7")
    private String currentFollowStatusIdName;

    /**
     * 当前跟进人名称
     */
    @Excel(name = "归属人", width = 20,orderNum = "8")
    private String currentFollowerName;

    /**
     * 跟进时间
     */
    @Excel(name = "跟进时间", width = 20,orderNum = "9",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date currentFollowTime;
}
