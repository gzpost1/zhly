package cn.cuiot.dmp.lease.dto.charge;

import cn.cuiot.dmp.common.bean.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 收费管理-催款记录
 *
 * @Author: zc
 * @Date: 2024-06-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChargeCollectionManageRecordQuery extends PageQuery {
    /**
     * 催款编号
     */
    private String id;

    /**
     * 企业ID 前端不用传
     */
    private Long companyId;

    /**
     * 客户id
     */
    private Long customerUserId;

    /**
     * 催款开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate beginTime;

    /**
     * 催款结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate endTime;
}