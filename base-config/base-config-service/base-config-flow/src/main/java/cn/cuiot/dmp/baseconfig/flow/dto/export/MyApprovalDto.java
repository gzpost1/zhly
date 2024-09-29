package cn.cuiot.dmp.baseconfig.flow.dto.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author pengjian
 * @create 2024/9/29 17:42
 */
@Data
public class MyApprovalDto extends ExportWorkOrderDto{

    @Excel(name = "工单状态",  width = 20)
    private String statusName;


    /**
     * 审批结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "节点完成时间", width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date entTime;
}
