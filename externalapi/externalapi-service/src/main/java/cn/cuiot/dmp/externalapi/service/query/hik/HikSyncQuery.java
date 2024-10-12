package cn.cuiot.dmp.externalapi.service.query.hik;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 海康同步query
 *
 * @Author: zc
 * @Date: 2024-10-12
 */
@Data
public class HikSyncQuery {

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginDate;

    /**
     * 结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
