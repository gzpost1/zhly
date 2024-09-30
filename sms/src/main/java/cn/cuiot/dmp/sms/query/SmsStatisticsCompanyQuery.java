package cn.cuiot.dmp.sms.query;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 短信统计企业相关信息query
 *
 * @Author: zc
 * @Date: 2024-09-24
 */
@Data
public class SmsStatisticsCompanyQuery {

    @NotBlank(message = "企业名称不能为空")
    private String companyName;
}
