package cn.cuiot.dmp.sms.query;

import lombok.Data;

/**
 * 签名列表query
 *
 * @Author: zc
 * @Date: 2024-09-25
 */
@Data
public class SmsSignListQuery {

    /**
     * 类型：0 默认，1 企业
     */
    private Byte type;
}
