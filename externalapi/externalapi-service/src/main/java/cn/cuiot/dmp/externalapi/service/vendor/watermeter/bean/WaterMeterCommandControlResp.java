package cn.cuiot.dmp.externalapi.service.vendor.watermeter.bean;

import cn.cuiot.dmp.externalapi.service.vendor.watermeter.constant.WaterMeterConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 物联网水表（山东科德）下发阀控指令响应信息
 *
 * @date 2024/8/21 14:00
 * @author gxp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class WaterMeterCommandControlResp implements Serializable {
    private static final long serialVersionUID = -6210571360260264923L;

    private String msg;

    private List<RespInfo> rows;


    /**
     * 返回信息
     *
     * @date 2024/8/26 11:40
     * @author gxp
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class RespInfo implements Serializable{
        private static final long serialVersionUID = 5811726442764785655L;

        private String success;

        private String message;

        /**
         * 表地址（对应设备的imei号）
         */
        private String steal_no;

        private String uuid;


        public Boolean success(){
            return WaterMeterConstant.WATER_METER_COMMAND_CONTROL_RESULT_CODE_SUCCESS.equals(success);
        }
    }

}
