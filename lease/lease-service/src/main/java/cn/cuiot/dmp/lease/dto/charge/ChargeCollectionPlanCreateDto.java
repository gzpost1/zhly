package cn.cuiot.dmp.lease.dto.charge;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 收费管理-催款计划 创建dto
 *
 * @Author: zc
 * @Date: 2024-06-25
 */
@Data
public class ChargeCollectionPlanCreateDto {
    /**
     * 计划名称
     */
    @NotBlank(message = "计划名称不能为空")
    @Length(max = 50, message = "计划名称限50字")
    private String name;

    /**
     * 通知渠道（1：系统消息；2：短信）
     */
    @NotNull(message = "通知渠道不能为空")
    private Byte channel;

    /**
     * 发送日期类型（1:每天，2:每周，3:每月）
     */
    @NotNull(message = "发送日期类型不能为空")
    private Byte cronType;

    /**
     * 执行频率-指定日期 1-31
     */
    private Integer cronAppointDay;

    /**
     * 执行频率-指定周数 1-7
     */
    private Integer cronAppointWeek;

    /**
     * 执行频率-指定的小时分
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date cronTime;

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
}