package cn.cuiot.dmp.sms.query;

import cn.cuiot.dmp.common.bean.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 短信统计
 *
 * @Author: zc
 * @Date: 2024-09-24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SmsStatisticsQuery extends PageQuery {

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 组织id
     */
    private Long deptId;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginDate;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
