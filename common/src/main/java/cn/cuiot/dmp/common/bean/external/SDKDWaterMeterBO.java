package cn.cuiot.dmp.common.bean.external;

import lombok.Data;

/**
 * 物联网水表（山东科德)-对接系统参数
 *
 * @Author: zc
 * @Date: 2024-08-21
 */
@Data
public class SDKDWaterMeterBO {

    /**
     * key
     */
    private String key;

    /**
     * ip+端口
     */
    private String ip;
}
