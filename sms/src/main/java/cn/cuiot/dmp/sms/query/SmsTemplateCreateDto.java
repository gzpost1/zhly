package cn.cuiot.dmp.sms.query;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 创建模板Dto
 *
 * @Author: zc
 * @Date: 2024-09-20
 */
@Data
public class SmsTemplateCreateDto {

    /**
     * 模板类型
     */
    @NotNull(message = "模板类型不能为空")
    private Byte smsType;

    /**
     * 标准模版id
     */
    @NotNull(message = "标准模版不能为空")
    private Integer stdTemplate;

    /**
     * 模板内容
     */
    @NotBlank(message = "模板内容不能为空")
    @Length(min = 1, max = 1978, message = "模板内容长度不能超过1978字")
    private String content;
}
