package cn.cuiot.dmp.pay.service.service.vo;

import cn.cuiot.dmp.pay.service.service.entity.BalanceChangeRecord;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
public class BalanceChangeRecordVo extends BalanceChangeRecord {

    /**
     * 应收id
     */
    private Long receivableId;

    /**
     * 实收id
     */
    private Long receivedId;

    /**
     * 所属账期-开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ownershipPeriodBegin;

    /**
     * 所属账期-结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ownershipPeriodEnd;

}
