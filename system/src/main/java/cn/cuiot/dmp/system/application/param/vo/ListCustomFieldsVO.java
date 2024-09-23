package cn.cuiot.dmp.system.application.param.vo;

import lombok.Data;

import java.util.LinkedHashSet;

/**
 * 列表自定义字段VO
 *
 * @Author: zc
 * @Date: 2024-09-03
 */
@Data
public class ListCustomFieldsVO {

    /**
     * 列表接口定义标识
     */
    private String identification;

    /**
     * 展示字段json列表
     */
    private LinkedHashSet<Object> fields;
}
