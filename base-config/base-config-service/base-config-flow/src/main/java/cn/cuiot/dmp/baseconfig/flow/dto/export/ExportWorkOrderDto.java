package cn.cuiot.dmp.baseconfig.flow.dto.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author pengjian
 * @create 2024/9/29 14:33
 */
@Data
public class ExportWorkOrderDto implements Serializable {

    @Excel(name = "工单编号", orderNum = "0", width = 20)
    private Long procInstId;

    @Excel(name = "业务类型", orderNum = "1", width = 20)
    private String businessTypeName;

    @Excel(name = "所属组织", orderNum = "2", width = 20)
    private String orgPath;

    @Excel(name = "工单流程", orderNum = "3", width = 20)
    private String workName;

    @Excel(name = "发起人", orderNum = "4", width = 20)
    private String userName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "发起时间",orderNum = "5",  width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
