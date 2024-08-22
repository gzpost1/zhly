package cn.cuiot.dmp.externalapi.service.vendor.watermeter.bean;

import cn.cuiot.dmp.externalapi.service.vendor.watermeter.constant.WaterMeterConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * 物联网水表（山东科德）下发阀控指令响应信息
 *
 * @date 2024/8/21 14:00
 * @author gxp
 */
@Data
public class WaterMeterCommandControlResp implements Serializable {
    private static final long serialVersionUID = -6210571360260264923L;

    /**
     * 调用状态
     */
    private String code;

    /**
     * 信息
     */
    private String message;

    /**
     *
     */
    private String commandId;

    /**
     * 状态
     */
    private String status;


    public Boolean success(){
        return WaterMeterConstant.WATER_METER_COMMAND_CONTROL_RESULT_CODE_SUCCESS.equals(code);
    }

}
