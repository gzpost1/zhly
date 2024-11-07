package cn.cuiot.dmp.externalapi.service.query.gw;


import cn.cuiot.dmp.common.bean.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 *
 * @author wuyongchong
 * @since 2024-10-23
 */
@Getter
@Setter
public class GwSmogEventQuery extends PageQuery {

    /**
    * 设备名称
    */
    private String name;

    /**
    * 设备IMEI号，全局唯一
    */
    private String imei;

    /**
     * 楼盘id
     */
    private Long buildingId;

    /**
     * 告警类型
     */
    private Integer alarmCode;

    /**
     * 告警开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    /**
     * 告警结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

}
