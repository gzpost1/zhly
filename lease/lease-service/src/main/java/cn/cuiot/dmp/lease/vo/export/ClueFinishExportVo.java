package cn.cuiot.dmp.lease.vo.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author hantingyao
 * @Description
 * @data 2024/9/30 9:54
 */
@Data
public class ClueFinishExportVo extends ClueExportVo{

    /**
     * 线索结果名称
     */
    @Excel(name = "线索结果", width = 20,orderNum = "7")
    private String resultIdName;

    /**
     * 线索备注
     */
    @Excel(name = "备注", width = 20,orderNum = "8")
    private String remark;

    /**
     * 完成人名称
     */
    @Excel(name = "完成人", width = 20,orderNum = "9")
    private String finishUserName;

    /**
     * 完成时间
     */
    @Excel(name = "完成时间", width = 20,orderNum = "10",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;
}
