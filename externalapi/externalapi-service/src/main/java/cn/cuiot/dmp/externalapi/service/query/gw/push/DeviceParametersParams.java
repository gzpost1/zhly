package cn.cuiot.dmp.externalapi.service.query.gw.push;

import lombok.Data;

import java.util.List;

/**
 * 格物门禁设备参数
 *
 * @Author: zc
 * @Date: 2024-09-12
 */
@Data
public class DeviceParametersParams {
    /**
     * 开门持续时间
     */
    private int doorOpenTime;
    /**
     * 重复认证时间间隔
     */
    private int dataFilterTime;
    /**
     * 体温报警阈值下限
     */
    private int lowestThermalThreshold;
    /**
     * 1V1人脸对比阀值
     */
    private int faceThreshold1V1;
    /**
     * 超阈值是否开门
     */
    private int isOpenExceededThreshold;
    /**
     * 是否开启活体检测
     */
    private int liveness;
    /**
     * 体温报警阈值上限
     */
    private int highestThermalThreshold;
    /**
     * 是否开启健康码功能
     */
    private int isOpenJourney;
    /**
     * 是否开启声音
     */
    private int enableVoice;
    /**
     * 设备sn
     */
    private String sn;
    /**
     * 认证方式
     */
    private List<Integer> authType;
    /**
     * 体温模式
     */
    private int temperatureModel;
    /**
     * 是否开启补光灯
     */
    private int lightOn;
}
