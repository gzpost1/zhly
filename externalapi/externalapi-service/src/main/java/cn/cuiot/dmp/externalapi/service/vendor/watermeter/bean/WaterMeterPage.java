package cn.cuiot.dmp.externalapi.service.vendor.watermeter.bean;

import cn.cuiot.dmp.externalapi.service.vendor.watermeter.constant.WaterMeterConstant;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 物联网水表（山东科德）分页
 *
 * @date 2024/8/21 14:00
 * @author gxp
 */
@Data
public class WaterMeterPage<R> implements Serializable {
    private static final long serialVersionUID = -8195569617473037685L;

    /**
     * 调用状态
     */
    private Integer code;

    /**
     * 信息
     */
    private String msg;

    /**
     * 总记录条数
     */
    private Integer total;

    /**
     * 数据
     */
    private List<R> data;


    public Boolean success(){
        return WaterMeterConstant.WATER_METER_REPORT_DATA_RESULT_CODE_SUCCESS.equals(code);
    }

}
