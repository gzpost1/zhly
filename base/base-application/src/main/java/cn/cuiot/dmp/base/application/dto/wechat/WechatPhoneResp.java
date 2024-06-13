package cn.cuiot.dmp.base.application.dto.wechat;

import lombok.Data;

/**
 * 手机号响应内容
 */
@Data
public class WechatPhoneResp {
    /**
     * 错误码 请求成功	(0) 系统繁忙，此时请开发者稍候再试(-1) 不合法的code（code不存在、已过期或者使用过）(40029)
     */
    private String errcode;

    /**
     *错误提示信息
     */
    private String errmsg;

    /**
     * 用户手机号信息
     */
    private PhoneInfo phone_info;
}
