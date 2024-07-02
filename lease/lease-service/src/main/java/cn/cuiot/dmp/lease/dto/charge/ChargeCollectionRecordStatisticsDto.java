package cn.cuiot.dmp.lease.dto.charge;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 收费管理-缴费管理-缴费记录统计Dto
 *
 * @Author: zc
 * @Date: 2024-06-25
 */
@Data
public class ChargeCollectionRecordStatisticsDto {
    /**
     * 用户id
     */
    private Long customerUserId;

    /**
     * 上次催款时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastNoticeTime;

    /**
     * 累计催款次数
     */
    private Integer totalNoticeNum;
}