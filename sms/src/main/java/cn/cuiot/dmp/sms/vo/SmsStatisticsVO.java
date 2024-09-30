package cn.cuiot.dmp.sms.vo;

import lombok.Data;

/**
 * 短信统计VO
 *
 * @Author: zc
 * @Date: 2024-09-24
 */
@Data
public class SmsStatisticsVO {

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 使用条数
     */
    private Integer number;

    public static final String COMPANY_ID = "companyId";
    public static final String NUMBER = "number";
}
