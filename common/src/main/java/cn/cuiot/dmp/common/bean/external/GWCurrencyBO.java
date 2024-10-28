package cn.cuiot.dmp.common.bean.external;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 格物通用对接系统参数
 *
 * @Author: zc
 * @Date: 2024-09-04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GWCurrencyBO extends ExternalapiBaseStatusBO {

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

    /**-----------------------------------------以下为业务需要传参-------------------------------------------------------*/

    /**
     * 产品deviceKey（如果需要dmp的转发数据就必传）
     */
    private String deviceKey;

    /**
     * 请求id（只负责业务传参）
     */
    private String requestId;

    /**
     * 企业id（只负责业务传参）
     */
    private Long companyId;
}
