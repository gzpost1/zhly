package cn.cuiot.dmp.app.dto.user;

import lombok.Data;

/**
 * @classname KaptchaResDTO
 * @description 图形验证响应求DTO
 * @author jiangze
 * @date 2020-06-18
 */
@Data
public class KaptchaResDTO {

    /**
     * 图形验证码
     */
    private String imageBase64;

    /**
     * 文本验证码
     */
    private String text;

    /**
     * 临时会话id
     */
    private String sid;

}