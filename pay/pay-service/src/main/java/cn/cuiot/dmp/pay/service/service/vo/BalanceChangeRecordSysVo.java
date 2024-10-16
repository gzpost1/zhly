package cn.cuiot.dmp.pay.service.service.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 余额明细变动表
 * </p>
 *
 * @author wuyongchong
 * @since 2023-11-29
 */
@Data
public class BalanceChangeRecordSysVo  {

    /**
     * 实收编码
     */
    @Excel(name = "实收编码", orderNum = "0", width = 20)
    private Long id;


    /**
     * 房屋id
     */
    private Long houseId;

    /**
     * 房屋名称
     */
    @Excel(name = "房屋名称", orderNum = "1", width = 20)
    private String houseName;


    /**
     * 金额
     */
    @Excel(name = "充值金额", orderNum = "2", width = 20)
    private Integer balance;


    /**
     * 备注
     */
    @Excel(name = "备注", orderNum = "3", width = 20)
    private String reason;

    /**
     * 充值方式
     */
    @Excel(name = "充值方式", orderNum = "4", width = 20)
    private String orderName;

    /**
     * 微信支付单号
     */
    @Excel(name = "交易单号", orderNum = "5", width = 20)
    private String payOrderId;

    private Long createUser;
    /**
     * 操作人名称
     */
    @Excel(name = "操作人", orderNum = "6", width = 20)
    private String createName;

    /**
     * 创建时间
     */
    @Excel(name = "充值人", orderNum = "7", width = 20)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;





}
