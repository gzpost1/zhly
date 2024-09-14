package cn.cuiot.dmp.externalapi.service.vendor.park.vo;

import cn.cuiot.dmp.externalapi.service.entity.park.VehicleExitRecordsEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jpedal.parser.shape.S;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author pengjian
 * @create 2024/9/9 18:49
 */
@Data
public class VehicleExitVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车场id
     */
    private Long parkId;

    /**
     * 车场名称
     */
    private String parkName;

    /**
     * 楼盘id
     */
    private Long communityId;

    /**
     * 楼盘名称
     */
    private String communityName;

    /**
     * 通道名称
     */
    private String nodeName;



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
     * 入出场时间
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

}
