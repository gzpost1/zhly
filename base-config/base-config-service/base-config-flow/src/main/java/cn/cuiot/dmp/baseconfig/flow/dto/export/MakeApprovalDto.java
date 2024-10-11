package cn.cuiot.dmp.baseconfig.flow.dto.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author pengjian
 * @create 2024/10/9 17:45
 */
@Data
public class MakeApprovalDto extends ExportWorkOrderDto {

    @Excel(name = "工单状态",orderNum = "6",  width = 20)
    private String statusName;

}
