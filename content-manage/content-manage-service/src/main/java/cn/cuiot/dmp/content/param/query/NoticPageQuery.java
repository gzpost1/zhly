package cn.cuiot.dmp.content.param.query;//	模板

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/29 19:39
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NoticPageQuery extends PageQuery {

    /**
     * 组织
     */
    private List<Long> departments;

    /**
     * 楼盘
     */
    private List<Long> buildings;

    /**
     * 标题
     */
    private String title;

    /**
     * 公告类型
     */
    private String type;

    /**
     * 状态
     */
    private Byte status;

    /**
     * 审核状态
     */
    private Byte auditStatus;

    /**
     * 发布状态
     */
    private Byte publishStatus;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 发布端
     */
    private Byte publishSource;
}
