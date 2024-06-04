package cn.cuiot.dmp.content.param.dto;//	模板

import lombok.Data;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/4 18:59
 */

@Data
public class ModuleApplicationCreateDto {

    /**
     * 模块id
     */
    private Long moduleId;

    /**
     * 名称
     */
    private String name;

    /**
     * 图片
     */
    private String pic;

    /**
     * 排序
     */
    private Byte sort;

    /**
     * 停启用
     */
    private Byte status;

    /**
     * 跳转类型
     */
    private Byte skipType;

    /**
     * 资源id
     */
    private String sourceId;

    /**
     * 跳转资源名称
     */
    private String sourceName;

    /**
     * 资源类型
     */
    private String sourceType;
}
