package cn.cuiot.dmp.externalapi.service.vo.gw.gasalarm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 格物-燃气报警器分页VO
 *
 * @Author: zc
 * @Date: 2024-10-24
 */
@Data
public class GwGasAlarmFaultRecordVO {

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 设备名称
     */
    @Excel(name = "设备名称", orderNum = "0", width = 20)
    private String deviceName;

    /**
     * 所属组织
     */
    @Excel(name = "所属组织", orderNum = "1", width = 20)
    private String deptPathName;

    /**
     * 楼盘id
     */
    private Long buildingId;

    /**
     * 所属楼盘名称
     */
    @Excel(name = "所属楼盘", orderNum = "2", width = 20)
    private String buildingName;

    /**
     * 设备IMEI
     */
    @Excel(name = "设备IMEI", orderNum = "3", width = 20)
    private String imei;

    /**
     * 告警类型
     */
    private String errorCode;

    /**
     * 告警类型名称
     */
    @Excel(name = "设备IMEI", orderNum = "4", width = 20)
    private String errorCodeName;

    /**
     * 故障时间
     */
    @Excel(name = "故障时间", orderNum = "5", exportFormat = "yyyy-MM-dd HH:mm:ss", width = 20)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date faultDate;
}
