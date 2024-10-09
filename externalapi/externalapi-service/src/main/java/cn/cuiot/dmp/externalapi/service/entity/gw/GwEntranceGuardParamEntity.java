package cn.cuiot.dmp.externalapi.service.entity.gw;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import cn.cuiot.dmp.base.infrastructure.persistence.handler.JsonTypeHandler;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 格物门禁-设备参数
 *
 * @Author: zc
 * @Date: 2024-09-11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "tb_gw_entrance_guard_param", autoResultMap = true)
public class GwEntranceGuardParamEntity extends YjBaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 门禁id
     */
    @TableField(value = "entrance_guard_id")
    private Long entranceGuardId;

    /**
     * 开门持续时间
     */
    @TableField(value = "door_open_time")
    private Integer doorOpenTime;

    /**
     * 重复认证时间间隔
     */
    @TableField(value = "data_filter_time")
    private Integer dataFilterTime;

    /**
     * 体温报警阈值下限
     */
    @TableField(value = "lowest_thermal_thresho_id")
    private Integer lowestThermalThreshoId;

    /**
     * 1V1人脸对比阀值
     */
    @TableField(value = "face_thresho_id_1v1")
    private Integer faceThreshoId1v1;

    /**
     * 1VN人脸对比阀值
     */
    @TableField(value = "face_thresho_id_1Vn")
    private Integer faceThreshoId1vn;

    /**
     * 超阈值是否开门
     */
    @TableField(value = "is_open_exceeded_thresho_id")
    private Integer isOpenExceededThreshoId;

    /**
     * 是否开启活体检测
     */
    @TableField(value = "liveness")
    private Integer liveness;

    /**
     * 体温报警阈值上限
     */
    @TableField(value = "highest_thermal_thresho_id")
    private Integer highestThermalThreshoId;

    /**
     * 是否开启健康码功能
     */
    @TableField(value = "is_open_journey")
    private Integer isOpenJourney;

    /**
     * 是否开启声音
     */
    @TableField(value = "enable_voice")
    private Integer enableVoice;

    /**
     * sn
     */
    @TableField(value = "sn")
    private String sn;

    /**
     * 认证方式
     */
    @TableField(value = "auth_type")
    private String authType;

    /**
     * 体温模式
     */
    @TableField(value = "temperature_model")
    private Integer temperatureModel;

    /**
     * 是否开启补光灯
     */
    @TableField(value = "light_on")
    private Integer lightOn;

    /**
     * 设备图片
     */
    @TableField(value = "images", typeHandler = JsonTypeHandler.class)
    private List<String> images;

    private static final long serialVersionUID = 1L;
}