package cn.cuiot.dmp.system.application.param.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @classname SecretKeyResDTO
 * @description 密钥响应求DTO
 * @author jiangze
 * @date 2023-11-08
 */
@Data
@AllArgsConstructor
public class SecretKeyResDTO {

    /**
     * 临时密钥id
     */
    private String kid;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 向量
     */
    private String iv;

}
