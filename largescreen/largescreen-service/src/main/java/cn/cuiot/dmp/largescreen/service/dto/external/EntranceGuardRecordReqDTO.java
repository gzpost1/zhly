package cn.cuiot.dmp.largescreen.service.dto.external;

import cn.cuiot.dmp.largescreen.service.dto.StatisInfoReqDTO;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class EntranceGuardRecordReqDTO extends StatisInfoReqDTO {


    /**
     * 人员名称
     */
    private String userName;


    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 楼盘id
     */
    private Long  buildingId;

    /**
     * 进出类型
     */
    private Integer inOut;


    /**
     * 是否通行
     */
    private Integer isOpenDoor;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordTimeStart;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordTimeEnd;

}
