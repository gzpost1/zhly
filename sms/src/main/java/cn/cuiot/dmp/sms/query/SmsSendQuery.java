package cn.cuiot.dmp.sms.query;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 短信发送query
 *
 * @Author: zc
 * @Date: 2024-09-23
 */
@Data
@Accessors(chain = true)
public class SmsSendQuery {

    /**
     * (必填)
     * 企业ID
     */
    private Long companyId;

    /**
     * (必填)
     * 手机号码，多个号码请用英文逗号隔开，例：18300000000,1830000000
     */
    private String mobile;

    /**
     * 发送参数
     */
    private List<String> params;

    /**
     * (必填)
     * 内部模板id
     */
    private Integer stdTemplate;
}
