package cn.cuiot.dmp.externalapi.service.entity.gw;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import cn.cuiot.dmp.base.infrastructure.persistence.handler.JsonTypeHandler;
import cn.cuiot.dmp.externalapi.service.query.gw.push.DeviceEventSmogParams;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 格物烟雾报警器事件信息
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(autoResultMap = true,value = "tb_gw_smog_event")
public class GwSmogEventEntity extends YjBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     *  id
     */
    @TableId("id")
    private Long id;


    /**
     * 烟雾报警器设备ID
     */
    private Long deviceId;


    /**
     * 告警状态
     */
    private Integer alarmCode;


    /**
     * 告警状态附带数据
     */
    private String alarmData;


    /**
     * 电池信息
     */
    @TableField(typeHandler = JsonTypeHandler.class)
    private DeviceEventSmogParams.BatteryInfo battery;


    /**
     * 连接服务上报
     */
    @TableField(typeHandler = JsonTypeHandler.class)
    private DeviceEventSmogParams.ConnectivityInfo connectivity;


    /**
     * 设备信息
     */
    @TableField(typeHandler = JsonTypeHandler.class)
    private DeviceEventSmogParams.Device deviceInfo;



}
