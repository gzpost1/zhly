package cn.cuiot.dmp.lease.dto.charge;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 收费管理-通知单 创建dto
 *
 * @author zc
 */
@Data
public class ChargeNoticeCreateDto {

    /**
     * 楼盘id列表
     */
    @NotEmpty(message = "楼盘id不能为空")
    private List<Long> buildings;

    /**
     * 收费项目列表
     */
    @NotEmpty(message = "收费项目不能为空")
    private List<Long> chargeItems;

    /**
     * 所属账期-开始时间
     */
    @NotNull(message = "所属账期开始时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ownershipPeriodBegin;

    /**
     * 所属账期-结束时间
     */
    @NotNull(message = "所属账期结束时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ownershipPeriodEnd;

    /**
     * 备注
     */
    @Length(max = 500, message = "备注容限500字")
    private String remark;
}