package cn.cuiot.dmp.baseconfig.flow.dto;

import cn.cuiot.dmp.query.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author pengjian
 * @create 2024/5/6 16:17
 */
@Data
public class QueryWorkPlanInfoDto extends PageQuery {

    /**
     * 计划id
     */
    private Long id;

    /**
     * 计划名称
     */
    private String planName;

    /**
     * 组织id
     */
    private Long orgId;

    /**
     * 组织结构及其下属机构
     */
    private List<Long> orgIds;

    /**
     * 日期区间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date  planDate;

    /**
     * 计划状态  0未开始 1进行中 2已结束
     */
    private Byte planState;

    /**
     * 0 停用 1 启用
     */
    private Byte state;

}
