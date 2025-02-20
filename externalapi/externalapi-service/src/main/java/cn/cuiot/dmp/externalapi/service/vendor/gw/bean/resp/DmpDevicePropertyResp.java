package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: zc
 * @Date: 2024-09-06
 */
@Data
@NoArgsConstructor
public class DmpDevicePropertyResp implements Serializable {

    private static final long serialVersionUID = 3272708880148264348L;

    /**
     * 属性标识符
     */
    private String key;

    /**
     * 属性值的单位。
     */
    private String unit;

    /**
     * 属性最新值。
     */
    private Object value;

    /**
     * 属性最近一次上报时间戳，毫秒。
     */
    private Long ts;

    public DmpDevicePropertyResp(String key, String unit, Object value, Long ts) {
        this.key = key;
        this.unit = unit;
        this.value = value;
        this.ts = ts;
    }

    public DmpDevicePropertyResp(String key) {
        this.key = key;
    }
}
