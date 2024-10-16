package cn.cuiot.dmp.lease.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 收费管理-通知单vo
 *
 * @author zc
 */
@Data
public class ChargeNoticeVo {
    /**
     * id
     */
    @Excel(name = "通知单号", orderNum = "0", width = 20)
    private Long id;

    /**
     * 收费项目
     */
    private List<Long> chargeItems;

    /**
     * 楼盘id列表
     */
    private List<Long> buildings;

    /**
     * 所属账期-开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "通知单月份开始日期",orderNum = "2",  width = 20,exportFormat = "yyyy-MM-dd")
    private Date ownershipPeriodBegin;

    /**
     * 所属账期-结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "通知单月份结束日期",orderNum = "3",  width = 20,exportFormat = "yyyy-MM-dd")
    private Date ownershipPeriodEnd;

    /**
     * 备注
     */
    private String remark;

    /**
     * 停启用状态（0停用，1启用）
     */
    private Byte status;

    /**
     * 状态
     */
    @Excel(name = "状态", orderNum = "7", width = 20, replace = {"启用_1", "停用_0"})
    private String statusName;
}