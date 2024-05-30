package cn.cuiot.dmp.content.param.vo;//	模板

import lombok.Data;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/28 15:32
 */
@Data
public class ContentImgTextVo {

    private Long id;

    /**
     * 组织
     */
    private String deparments;

    /**
     * 楼盘
     */
    private String buildings;

    /**
     * 图文标题
     */
    private String title;

    /**
     * 图文类型
     */
    private Byte type;

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
