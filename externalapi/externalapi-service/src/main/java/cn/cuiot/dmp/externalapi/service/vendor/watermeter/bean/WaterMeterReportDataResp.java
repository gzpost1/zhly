package cn.cuiot.dmp.externalapi.service.vendor.watermeter.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 物联网水表（山东科德）上报数据响应信息
 *
 * @author gxp
 * @date 2024/8/21 14:00
 */
@Data
public class WaterMeterReportDataResp implements Serializable {
    private static final long serialVersionUID = -4022486778015320703L;

    /**
     *
     */
    private Long id;

    /**
     * 水表imei
     */
    private String wsImei;

    /**
     * 水表imsi
     */
    private String wsImsi;

    /**
     * 上传时间
     */
    private String createDate;

    /**
     * 信号质量
     */
    private String wsCsq;

    /**
     * 电池电压
     */
    private String wsBatteryvoltage;

    /**
     * 累计水量
     */
    private String wsCumulativeamount;

    /**
     * 阀门状态
     */
    private String valveStatus;

    /**
     *
     */
    private String sbstatus;

    /**
     *
     */
    private String csq;

    /**
     *
     */
    private String snr;

    /**
     *
     */
    private String rsrq;

    /**
     *
     */
    private String rsrp;

    /**
     *
     */
    private String ecl;

    /**
     *
     */
    private String ysStr4;

    /**
     *
     */
    private String waterMeterInterval;

    /**
     *
     */
    private String waterMeterIntervalTime;

    /**
     *
     */
    private Object params;
}
