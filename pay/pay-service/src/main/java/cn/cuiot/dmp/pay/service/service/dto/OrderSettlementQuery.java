package cn.cuiot.dmp.pay.service.service.dto;

import cn.cuiot.dmp.query.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description 结算报表查询
 * @Date 2024/10/11 16:48
 * @Created by libo
 */
@Data
public class OrderSettlementQuery extends PageQuery {

    /**
     * 应收编码
     */
    private String receivableId;

    /**
     * 实收编码
     */
    private String paidUpId;

    /**
     * 交易单号
     */
    private String transactionNo;

    /**
     * 收支类型 0收入 1支出
     */
    private Byte incomeType;

    /**
     * 收费项目id
     */
    private Long chargeItemId;

    /**
     * 交易方式
     */
    private Long transactionMode;

    /**
     * 支出项目 0支付手续费
     */
    private Byte expenditureType;

    /**
     * 所属楼盘id
     */
    private List<Long> loupanIds;

    /**
     * 房屋id
     */
    private List<Long> houseIds;

    /**
     * 结算日期-开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 结算日期-开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 企业ID
     */
    private Long companyId;
}

