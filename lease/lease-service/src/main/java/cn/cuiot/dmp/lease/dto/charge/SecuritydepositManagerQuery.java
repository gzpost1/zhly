package cn.cuiot.dmp.lease.dto.charge;

import cn.cuiot.dmp.common.bean.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description 押金管理分页查询
 * @Date 2024/6/14 10:28
 * @Created by libo
 */
@Data
public class SecuritydepositManagerQuery extends PageQuery {
    /**
     * 应收编码/实收编码
     */
    private String receivableCode;

    /**
     * 客户id
     */
    private Long customerUserId;

    /**
     * 房屋id
     */
    private Long houseId;

    /**
     * 房屋id根据名称
     */
    private Long houseIdByName;

    /**
     * 房屋id根据编码
     */
    private Long houseIdByCode;

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

    /**
     * 状态 0未交款 1已交清 2未退完 3 已退完 4作废
     */
    private Byte status;

    /**
     * 企业id
     */
    private Long companyId;
    /**
     * 是否已经实收 0未实收 1已实收
     */
    private Byte selectReceived;

}
