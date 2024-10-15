package cn.cuiot.dmp.externalapi.service.vo;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 门禁设备通行记录统计
 *
 * @Author: xiaotao
 * @Date: 2024-08-22
 */
@Data
public class EntranceGuardRecordVo {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;


    /**
     * 电话号码
     */
    private String phoneNumber;


    /**
     * 楼栋id
     */
    private Long buildingId;


    /**
     * 楼栋名称
     */
    private String buildingName;

    /**
     * 设备id
     */
    private Long equipmentId;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 进出门方向
     */
    private Integer inOut;

    /**
     * 是否通行
     */
    private Integer isOpenDoor;

    /**
     * 记录时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordTime;


    /**
     * 记录图片
     */
    private String recordImg;
}
