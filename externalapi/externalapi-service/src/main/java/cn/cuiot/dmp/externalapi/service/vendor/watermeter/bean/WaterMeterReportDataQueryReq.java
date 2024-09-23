package cn.cuiot.dmp.externalapi.service.vendor.watermeter.bean;

import cn.cuiot.dmp.externalapi.service.vo.watermeter.WaterMeterQueryVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 物联网水表（山东科德）上报数据请求参数
 *
 * @date 2024/8/21 14:13
 * @author gxp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class WaterMeterReportDataQueryReq implements Serializable {
    private static final long serialVersionUID = -2296079048473860663L;

    /**
     * 开始时间 yyyy-MM-dd
     */
    private String beginTime;

    /**
     * 结束时间 yyyy-MM-dd
     */
    private String endTime;

    /**
     * 设备的imei号
     */
    private String wsImei;

    public WaterMeterReportDataQueryReq(WaterMeterQueryVO vo) {
        this.wsImei = vo.getWsImei();
    }
}
