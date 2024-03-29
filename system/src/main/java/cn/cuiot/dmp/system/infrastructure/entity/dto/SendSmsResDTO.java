package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * @author jiangze
 * @classname SendSmsReqDTO
 * @description 发送短信响应DTO
 * @date 2020-07-09
 */
@Data
public class SendSmsResDTO {

    /**
     * 返回数据
     */
    private String data;

    /**
     * 返回提醒
     */
    private String message;

    /**
     * 状态码
     */
    private String status;

}


