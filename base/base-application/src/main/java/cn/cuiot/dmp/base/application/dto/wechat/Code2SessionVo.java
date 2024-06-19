package cn.cuiot.dmp.base.application.dto.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Code2SessionVo
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Code2SessionVo {

    /**
     * 用户唯一标识
     */
    @JsonProperty("openid")
    private String openId;
    /**
     * 会话密钥
     */
    @JsonProperty("session_key")
    private String sessionKey;
    /**
     * 错误码
     */
    @JsonProperty("errcode")
    private String errCode;
    /**
     * 错误信息
     */
    @JsonProperty("errmsg")
    private String errMsg;
    /**
     * 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台账号下会返回
     */
    @JsonProperty("unionid")
    private String unionId;
}
