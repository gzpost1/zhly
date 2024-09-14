package cn.cuiot.dmp.common.bean.external;

import lombok.Data;

/**
 * 门禁（格物）-对接系统参数
 *
 * @Author: zc
 * @Date: 2024-09-04
 */
@Data
public class GWEntranceGuardBO {

    /**
     * 应用appId
     */
    private String appId;

    /**
     * 应用appSecret
     */
    private String appSecret;

    /**
     * 产品productKey
     */
    private String productKey;

    /**
     * 产品deviceKey（如果需要dmp的转发数据就必传）
     */
    private String deviceKey;

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 企业id（只负责业务传参）
     */
    private Long companyId;
}
