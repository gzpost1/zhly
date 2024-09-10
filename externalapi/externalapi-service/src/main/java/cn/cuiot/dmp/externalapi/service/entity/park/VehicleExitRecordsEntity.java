package cn.cuiot.dmp.externalapi.service.entity.park;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 车辆进出信息
 * @author pengjian
 * @since 2024-09-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_vehicle_exit_records")
public class VehicleExitRecordsEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车场id
     */
    @TableId("park_id")
    private Integer parkId;


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
