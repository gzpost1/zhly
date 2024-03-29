package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangze
 * @classname SendSmsReqDTO
 * @description 发送短信请求DTO
 * @date 2020-07-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendSmsReqDTO {

    /**
     * 签名信息
     */
    private String token;

    /**
     * 接入标识码
     */
    @JsonProperty(value = "app_id")
    private String appId;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 序列号
     */
    @JsonProperty(value = "trans_id")
    private String transId;

    /**
     * 数据
     */
    private SendSmsReqDataDTO data;

}


