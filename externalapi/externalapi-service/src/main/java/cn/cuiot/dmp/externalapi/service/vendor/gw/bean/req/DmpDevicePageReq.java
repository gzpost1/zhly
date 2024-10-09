package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zc
 * @Date: 2024-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DmpDevicePageReq extends BaseDmpPageReq {

    private static final long serialVersionUID = 7476215560731918017L;

    /**
     * 设备所属的产品key。
     */
    private String productKey;

}
