package cn.cuiot.dmp.externalapi.service.query.gw.entranceguard;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 格物门禁-修改参数
 *
 * @Author: zc
 * @Date: 2024-09-11
 */
@Data
public class GwEntranceGuardParamDto {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 门禁id
     */
    private Long entranceGuardId;

    /**
     * 开门持续时间
     */
    private Integer doorOpenTime;

    /**
     * 重复认证时间间隔
     */
    private Integer dataFilterTime;

    /**
     * 体温报警阈值下限
     */
    private Integer lowestThermalThreshoId;

    /**
     * 1V1人脸对比阀值
     */
    private Integer faceThreshoId1v1;

    /**
     * 1VN人脸对比阀值
     */
    private Integer faceThreshoId1vn;

    /**
     * 超阈值是否开门
     */
    private Integer isOpenExceededThreshoId;

    /**
     * 是否开启活体检测
     */
    private Integer liveness;

    /**
     * 体温报警阈值上限
     */
    private Integer highestThermalThreshoId;

    /**
     * 是否开启健康码功能
     */
    private Integer isOpenJourney;

    /**
     * 是否开启声音
     */
    private Integer enableVoice;

    /**
     * sn
     */
    private String sn;

    /**
     * 认证方式
     */
    private String authType;

    /**
     * 体温模式
     */
    private Integer temperatureModel;

    /**
     * 是否开启补光灯
     */
    private Integer lightOn;

    /**
     * 设备图片
     */
    private List<String> images;
}
