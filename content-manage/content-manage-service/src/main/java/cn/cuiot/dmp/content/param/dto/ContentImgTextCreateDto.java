package cn.cuiot.dmp.content.param.dto;//	模板

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/28 15:52
 */
@Data
public class ContentImgTextCreateDto {

    /**
     * 组织
     */
    private List<Long> departments;

    /**
     * 楼盘
     */
    private List<Long> buildings;

    /**
     * 图文标题
     */
    @NotEmpty(message = "图文标题不能为空")
    private String title;

    /**
     * 图文类型
     */
    private Long type;

    /**
     * 图文封面
     */
    private String coverPic;

    /**
     * 摘要
     */
    private String digest;

    /**
     * 详情
     */
    private String detail;
}
