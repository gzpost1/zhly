package cn.cuiot.dmp.gateway.dto;

import lombok.Data;

/**
 * 签名对象信息
 *
 * @author lixf
 */
@Data
public class SignatureConfigDto {
    /**
     * 应用名称
     */
    private String name;
    /**
     * 密钥
     */
    private String accessKey;
    /**
     * 客户端
     */
    private String clientId;
}
