package cn.cuiot.dmp.externalapi.service.vendor.park.query;


import java.util.Date;
import java.io.Serializable;
import java.util.List;

import cn.cuiot.dmp.common.bean.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author pengjian
 * @since 2024-09-09
 */
@Getter
@Setter
public class VehicleExitRecordsQuery extends PageQuery {
    /**
    * 通道id
    */
    private Integer nodeId;

    /**
    * 车牌号
    */
    private String plateNo;

    /**
    * 进出标志0-进 1-出
    */
    private Integer capFlag;

    /**
    * 车辆进出场图片名称
    */
    private String imgName;

    /**
    * 时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date capTime;

    /**
    * 图片信息类型
    */
    private Integer imgType;

    /**
    * 图片信息
    */
    private String imgInfo;

    /**
    * 车辆进出站地点
    */
    private String capPlace;

    /**
    * 固定车辆类型
    */
    private Integer carType;

    /**
    * 车颜色黑、白、蓝、红等
    */
    private String carColor;

    /**
    * 车辆类型
    */
    private String passStyle;

    /**
    * 放行类型
    */
    private Integer passType;

    /**
    * 出入场唯一记录
    */
    private String trafficId;

    /**
    * 流水号
    */
    private String carSerial;

    /**
    * 车主名称
    */
    private String carOwnerName;

    /**
    * 操作人员
    */
    private String operName;


    /**
     * 楼盘id
     */
    private List<Long> communityIds;

    /**
     * 车场id
     */
    private Long parkId;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}
