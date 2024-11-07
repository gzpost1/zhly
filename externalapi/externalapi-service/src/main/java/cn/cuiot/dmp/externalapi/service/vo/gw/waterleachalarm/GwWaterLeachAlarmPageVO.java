package cn.cuiot.dmp.externalapi.service.vo.gw.waterleachalarm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 格物-水浸报警器分页vo
 *
 * @Author: zc
 * @Date: 2024-10-23
 */
@Data
public class GwWaterLeachAlarmPageVO {
    /**
     * id
     */
    private Long id;

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
     * 状态 1-启用 0-停用
     */
    @Excel(name = "启用状态", orderNum = "4", replace = {"停用_0", "启用_1"}, width = 20)
    private Byte status;

    /**
     * 设备状态 (0: 在线，1: 离线，2: 未激活）接口返回
     */
    @Excel(name = "设备状态", orderNum = "5", replace = {"在线_0", "离线_1", "未激活_2"}, width = 10)
    private String equipStatus;

    @Excel(name = "创建时间", orderNum = "6", exportFormat = "yyyy-MM-dd HH:mm:ss", width = 20)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
