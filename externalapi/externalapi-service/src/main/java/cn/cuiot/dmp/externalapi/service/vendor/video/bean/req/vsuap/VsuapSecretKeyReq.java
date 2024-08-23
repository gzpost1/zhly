package cn.cuiot.dmp.externalapi.service.vendor.video.bean.req.vsuap;

import lombok.Data;

/**
 * 云智眼-密钥 req
 *
 * @Author: zc
 * @Date: 2024-08-21
 */
@Data
public class VsuapSecretKeyReq {

    /**
     * 云智眼AK
     */
    private String accessKey;

    /**
     * 云智眼SK
     */
    private String secretKey;

    /**
     * 企业id
     */
    private Long companyId;
}
