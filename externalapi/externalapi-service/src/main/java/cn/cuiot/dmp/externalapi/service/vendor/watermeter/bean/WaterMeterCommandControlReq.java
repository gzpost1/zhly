package cn.cuiot.dmp.externalapi.service.vendor.watermeter.bean;

import cn.cuiot.dmp.externalapi.service.vo.watermeter.WaterMeterOperateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 物联网水表（山东科德）下发阀控指令请求
 *
 * @date 2024/8/21 14:13
 * @author gxp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class WaterMeterCommandControlReq implements Serializable {
    private static final long serialVersionUID = -8527541751522467489L;

    private List<CommandControlInfo> rows;


    /**
     * 阀控指令信息
     *
     * @date 2024/8/26 11:28
     * @author gxp
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class CommandControlInfo implements Serializable{
        private static final long serialVersionUID = 5369409130166747581L;
        /**
         * 表地址（对应设备的imei号）
         */
        private String steal_no;

        /**
         * 对设备执行的阀门操作 0开阀 1关阀
         */
        private String valve_controll;

        /**
         * 操作id
         */
        private String operate_id;

        public CommandControlInfo(WaterMeterOperateVO operateVO) {
            this.steal_no = operateVO.getImei();
            this.valve_controll = Integer.valueOf(operateVO.getValveControlType()).toString();
            this.operate_id = "";
        }

    }

}
