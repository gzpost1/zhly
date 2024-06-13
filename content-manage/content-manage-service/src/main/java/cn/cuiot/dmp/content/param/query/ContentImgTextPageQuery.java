package cn.cuiot.dmp.content.param.query;//	模板

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/29 15:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContentImgTextPageQuery extends PageQuery {

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 组织
     */
    private List<Long> departments;

    /**
     * 楼盘
     */
    private List<Long> buildings;

    /**
     * 审核状态
     */
    private Byte auditStatus;

    /**
     * 状态
     */
    private Byte status;

    /**
     * 图文标题
     */
    private String title;

    /**
     * 图文类型
     */
    private Long type;
}
