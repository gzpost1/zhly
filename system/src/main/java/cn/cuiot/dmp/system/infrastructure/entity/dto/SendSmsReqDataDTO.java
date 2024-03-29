package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangze
 * @classname SendSmsReqDataDTO
 * @description 发送短信请求消息体DTO
 * @date 2020-07-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendSmsReqDataDTO {

    /**
     * 系统名
     */
    private String callerSystemName;

    /**
     * 短信内容
     */
    private String content;

    /**
     * 接收者手机号
     */
    private String receive;

}
