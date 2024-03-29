package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangze
 * @classname SimpleStringResDTO
 * @description 字符串类型结果响应DTO
 * @date 2020-07-14
 */
@Data
@NoArgsConstructor
public class SimpleStringResDTO {

    /**
     * 注册返回消息
     */
    private String message;

    /**
     * 短信验证码文本
     */
    private String text;

    /**
     * sid
     */
    private String sid;

    public SimpleStringResDTO(String message) {
        this.message = message;
    }

    public SimpleStringResDTO(String message, String text, String sid) {
        this.message = message;
        this.text = text;
        this.sid = sid;
    }

}
