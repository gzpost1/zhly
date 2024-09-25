package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zc
 * @Date: 2024-09-06
 */
@Data
public class DmpDeviceReq implements Serializable {

    private static final long serialVersionUID = 817210261874835650L;

    /**
     * 产品的productKey。创建产品时，雁飞·格物DMP平台为该产品颁发的全局唯一标识。
     */
    private String productKey;

    /**
     * 设备的deviceKey，由用户在添加设备时指定但需要满足该账户下唯一。
     */
    private String deviceKey;

    /**
     * 雁飞·格物DMP平台唯一标识码。说明如果传入该参数，则无需传入productKey和deviceKey。
     * iotId作为设备唯一标识码，和productKey与deviceKey组合是一一对应的关系。
     * 如果您同时传入iotId和productKey与deviceKey组合，则以iotId为准。
     */
    private String iotId;

}
