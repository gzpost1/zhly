package cn.cuiot.dmp.sms.query;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 发送签名设置
 *
 * @Author: zc
 * @Date: 2024-09-20
 */
@Data
public class SmsSendSignSetQuery {

    /**
     * 签名id
     */
    @NotNull(message = "签名不能为空")
    private Long signId;

    /**
     * 类型：0 默认，1 企业
     */
    @NotNull(message = "签名类型不能为空")
    private Byte type;
}
