package cn.cuiot.dmp.system.application.param.dto.auth;

import java.io.Serializable;
import lombok.Data;

/**
 * 发送短信响应
 * @author: wuyongchong
 * @date: 2024/5/24 14:09
 */
@Data
public class SmsCodeResDto implements Serializable {

    /**
     * 是否成功
     */
    private Boolean sendSucceed;
    /**
     * 提示
     */
    private String message;

    public SmsCodeResDto() {
    }

    public SmsCodeResDto(Boolean sendSucceed, String message) {
        this.sendSucceed = sendSucceed;
        this.message = message;
    }
}
