package cn.cuiot.dmp.lease.vo;

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
    private Date ownershipPeriodBegin;

    /**
     * 所属账期-结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ownershipPeriodEnd;

    /**
     * 备注
     */
    private String remark;
}