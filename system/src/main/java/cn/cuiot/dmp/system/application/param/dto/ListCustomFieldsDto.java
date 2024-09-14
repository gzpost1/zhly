package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.LinkedHashSet;

/**
 * 列表自定义字段
 *
 * @Author: zc
 * @Date: 2024-09-03
 */
@Data
public class ListCustomFieldsDto {

    /**
     * 企业ID(前端不用传)
     */
    private Long companyId;

    /**
     * 列表接口定义标识
     */
    @NotBlank(message = "标识不能为空")
    private String identification;

    /**
     * 展示字段json列表
     */
    private LinkedHashSet<Object> fields;
}
