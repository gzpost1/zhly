package cn.cuiot.dmp.sms.vendor.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 申请签名req
 *
 * @Author: zc
 * @Date: 2024-09-23
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class SmsBindSignReq {

    /**
     * 需要申请的签名，例如：【签名】
     */
    private String SignName;
}
