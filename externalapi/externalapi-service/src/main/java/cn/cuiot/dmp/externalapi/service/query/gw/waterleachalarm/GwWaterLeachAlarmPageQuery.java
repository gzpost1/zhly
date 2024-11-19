package cn.cuiot.dmp.externalapi.service.query.gw.waterleachalarm;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 格物-水浸报警器分页query
 *
 * @Author: zc
 * @Date: 2024-10-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GwWaterLeachAlarmPageQuery extends PageQuery {

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
     * 状态 1-启用 0-停用
     */
    private Byte status;

    /**
     * 设备状态 (0: 在线，1: 离线，2: 未激活）接口返回
     */
    private String equipStatus;
}
