package cn.cuiot.dmp.baseconfig.flow.dto;

import cn.cuiot.dmp.query.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author pengjian
 * @create 2024/5/8 14:31
 */
@Data
public class QueryPlanExecutionDto extends PageQuery {

    /**
     * 工单id
     */
    private Long procInstId;

    /**
     * 生成开始时间 带00：00：00
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    /**
     * 生成结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;

    /**
     * 页面标识 1 已生成  0 未生成与生成失败
     */
    private Byte pageType;

    /**
     * 1已生成 0 未生成 2 生成失败
     */
    private Byte  state;

    /**
     * 计划id
     */
    private Long planId;
}
