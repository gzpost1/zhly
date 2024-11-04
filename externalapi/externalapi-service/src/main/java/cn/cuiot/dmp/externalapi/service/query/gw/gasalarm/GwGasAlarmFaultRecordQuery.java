package cn.cuiot.dmp.externalapi.service.query.gw.gasalarm;

import cn.cuiot.dmp.common.bean.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 格物-水浸报警器报警记录query
 *
 * @Author: zc
 * @Date: 2024-10-24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GwGasAlarmFaultRecordQuery extends PageQuery {

    /**
     * 企业id（前端不用传）
     */
    private Long companyId;

    /**
     * 楼盘id
     */
    private List<Long> buildingIds;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备IMEI
     */
    private String imei;

    /**
     * 告警类型
     */
    private String errorCode;

    /**
     * 故障开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date faultBeginDate;

    /**
     * 故障开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date faultEndDate;
}
