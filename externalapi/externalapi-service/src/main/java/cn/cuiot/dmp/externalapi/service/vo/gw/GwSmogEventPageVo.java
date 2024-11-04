package cn.cuiot.dmp.externalapi.service.vo.gw;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 *
 * @author wuyongchong
 * @since 2024-10-23
 */
@Getter
@Setter
public class GwSmogEventPageVo  {

    /**
    * 设备名称
    */
    @Excel(name = "设备名称", orderNum = "0", width = 20)
    private String name;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 部门名称
     */
    @Excel(name = "所属组织", orderNum = "1", width = 20)
    private String deptName;

    /**
     * 楼盘id
     */
    private Long buildingId;

    /**
     * 楼盘名称
     */
    @Excel(name = "所属楼盘", orderNum = "2", width = 20)
    private String buildingName;

    /**
    * 设备IMEI号，全局唯一
    */
    @Excel(name = "设备IMEI", orderNum = "3", width = 20)
    private String imei;


    /**
     * 告警类型
     */
    @Excel(name = "告警类型", orderNum = "4", width = 20)
    private Integer alarmCode;

    /**
     * 告警信息
     */
    @Excel(name = "告警详情", orderNum = "5", width = 20)
    private String alarmData;


    /**
     * 告警时间
     */
    @Excel(name = "告警时间", orderNum = "6", width = 20)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
