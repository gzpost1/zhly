package cn.cuiot.dmp.sms.vendor.resp;

import cn.cuiot.dmp.sms.enums.SmsHttpRespCode;
import lombok.Data;

/**
 * 短信基本响应参数
 *
 * @Author: zc
 * @Date: 2024-09-20
 */

@Data
public class SmsBaseResp<T> {

    /**
     * 错误代码：0 成功，更多返回错误代码请看首页的错误代码描述
     */
    private Integer code;

    /**
     * 错误描述
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    public Boolean isSuccess() {
        return SmsHttpRespCode.SUCCESS.getCode().equals(code);
    }
}
