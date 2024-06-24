package cn.cuiot.dmp.lease.dto.charge;

import cn.cuiot.dmp.common.bean.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description 收费管理-收银台-缴费管理-分页查询
 * @Date 2024/6/12 11:32
 * @Created by libo
 */
@Data
public class TbChargeManagerQuery extends PageQuery {
    /**
     * 房屋id
     */
    private Long houseId;

    /**
     * 应收编码
     */
    private String receivableCode;

    /**
     * 应收状态 0未交款 1已交款 2已交清
     */
    private Byte receivbleStatus;

    /**
     * 挂起状态 0未挂起 1已挂起
     */
    private Byte hangUpStatus;

    /**
     * 作废状态 0正常 1已作废
     */
    private Byte abrogateStatus;

    /**
     * 收费项目id
     */
    private Long chargeItemId;

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
     * 应收日期-开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date dueDateBegin;

    /**
     * 应收日期-结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date dueDateEnd;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 自动生成计划编码
     */
    private Long receivblePlanId;
}
