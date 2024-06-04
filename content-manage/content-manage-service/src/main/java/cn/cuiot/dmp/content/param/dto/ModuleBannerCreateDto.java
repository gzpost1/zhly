package cn.cuiot.dmp.content.param.dto;//	模板

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/4 10:36
 */
@Data
public class ModuleBannerCreateDto {

    /**
     * 模块id
     */
    @NotNull(message = "模块id不能为空")
    private Long moduleId;

    /**
     * 名称
     */
    @NotEmpty(message = "名称不能为空")
    private String name;

    /**
     * 图片
     */
    private String pic;

    /**
     * 生效开始时间
     */
    private Date effectiveStartTime;

    /**
     * 生效结束时间
     */
    private Date effectiveEndTime;

    /**
     * 是否跳转
     */
    private Byte skip;

    /**
     * 跳转类型
     */
    private Byte skipType;

    /**
     * 资源id
     */
    private Long sourceId;

    /**
     * 跳转资源名称
     */
    private String sourceName;

    /**
     * 资源类型
     */
    private String sourceType;
}
