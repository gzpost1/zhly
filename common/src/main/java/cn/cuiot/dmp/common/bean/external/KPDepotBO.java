package cn.cuiot.dmp.common.bean.external;

import lombok.Data;

/**
 * 停车场（科拓）-对接系统参数
 *
 * @Author: zc
 * @Date: 2024-09-04
 */
@Data
public class KPDepotBO {

    /**
     * appid
     */
    private String appId;

    /**
     * appSercert
     */
    private String appSercert;

    /**
     * parkId
     */
    private String parkId;
}
