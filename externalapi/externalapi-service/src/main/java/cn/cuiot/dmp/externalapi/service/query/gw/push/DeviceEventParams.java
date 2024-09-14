package cn.cuiot.dmp.externalapi.service.query.gw.push;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 格物门禁事件推送
 *
 * @Author: zc
 * @Date: 2024-09-12
 */
@Data
public class DeviceEventParams {

    /**
     * 设备sn
     */
    private String sn;

    /**
     * 认证类型
     */
    private Integer scanTpye;

    /**
     * 进出门方向
     */
    private Integer inOut;

    /**
     * 是否开门
     */
    private Integer isOpenDoor;

    /**
     * 识别时间戳
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date snapTime;

    /**
     * 图片方式
     */
    private Integer imageType;

    /**
     * 图片
     */
    private String image;

    /**
     * 体温
     */
    private Double temperature;

    /**
     * 体温报警阈值上限
     */
    private Double highestThermalThreshold;

    /**
     * 体温报警阈值下限
     */
    private Double lowestThermalThreshold;

    /**
     * 体温类型
     */
    private Integer temperatureType;

    /**
     * 健康码相关数据
     */
    private QrCode qrCode;

    /**
     * 疫苗相关数据
     */
    private Vaccine vaccine;

    /**
     * 核酸检测相关信息
     */
    private String nucleic;

    /**
     * 行程码相关信息
     */
    private String journey;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 用户信息
     */
    private Info info;

    /**
     * 异常原因
     */
    private Integer reason;

    @Data
    public static class Info {
        /**
         * 用户id
         */
        private String personId;
        /**
         * 用户名称
         */
        private String name;
    }

    @Data
    public static class Vaccine {
        /**
         * 疫苗相关数据
         */
        private Integer vaccinationStatus;
    }

    @Data
    public static class QrCode {
        /**
         * 健康码相关数据
         */
        private Integer qrCodeStatus;
    }
}
