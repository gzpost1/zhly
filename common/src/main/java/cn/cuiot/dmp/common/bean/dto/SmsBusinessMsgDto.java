package cn.cuiot.dmp.common.bean.dto;//	模板

import lombok.Data;

import java.util.List;

/**
 * 业务短信消息
 *
 * @Author: zc
 * @Date: 2024-06-26
 */
@Data
public class SmsBusinessMsgDto {

    /**
     * 手机号
     */
    private String telNumbers;

    /**
     * 参数
     */
    List<String> params;

    /**
     * 模板id
     */
    Integer templateId;

    /**
     * 企业ID
     */
    private Long companyId;
}