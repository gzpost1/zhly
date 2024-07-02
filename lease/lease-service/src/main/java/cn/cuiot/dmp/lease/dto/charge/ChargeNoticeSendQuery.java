package cn.cuiot.dmp.lease.dto.charge;

import cn.cuiot.dmp.common.bean.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 收费管理-通知单分页query
 *
 * @Author: zc
 * @Date: 2024-06-24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChargeNoticeSendQuery extends PageQuery {

    /**
     * 通知单号
     */
    @NotNull(message = "通知单号不能为空")
    private Long id;

    /**
     * 消息发送类型（1：系统消息；2：短信）
     */
    @NotNull(message = "消息发送类型不能为空")
    private Byte msgType;

    /**
     * 企业ID 前端不用传
     */
    private Long companyId;

    /**
     * 所属账期-开始时间 前端不用传
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ownershipPeriodBegin;

    /**
     * 所属账期-结束时间 前端不用传
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ownershipPeriodEnd;
}