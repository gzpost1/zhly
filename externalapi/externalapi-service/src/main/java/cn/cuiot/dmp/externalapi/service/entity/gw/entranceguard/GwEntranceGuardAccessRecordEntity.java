package cn.cuiot.dmp.externalapi.service.entity.gw.entranceguard;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 格物门禁 通过记录
 *
 * @Author: zc
 * @Date: 2024-09-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "tb_gw_entrance_guard_access_record")
public class GwEntranceGuardAccessRecordEntity extends YjBaseEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 设备sn
     */
    @TableField(value = "sn")
    private String sn;

    /**
     * 认证类型
     */
    @TableField(value = "scan_tpye")
    private Integer scanTpye;

    /**
     * 进出门方向
     */
    @TableField(value = "in_out")
    private Integer inOut;

    /**
     * 是否开门
     */
    @TableField(value = "is_open_door")
    private Integer isOpenDoor;

    /**
     * 识别时间戳
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "snap_time")
    private Date snapTime;

    /**
     * 图片方式
     */
    @TableField(value = "image_type")
    private Integer imageType;

    /**
     * 图片
     */
    @TableField(value = "image")
    private String image;

    /**
     * 体温类型
     */
    @TableField(value = "temperature_type")
    private Integer temperatureType;

    /**
     * 体温
     */
    @TableField(value = "temperature")
    private Double temperature;

    /**
     * 体温报警阈值上限
     */
    @TableField(value = "highest_thermal_threshold")
    private Double highestThermalThreshold;

    /**
     * 体温报警阈值下限
     */
    @TableField(value = "lowest_thermal_threshold")
    private Double lowestThermalThreshold;

    /**
     * 健康码相关数据
     */
    @TableField(value = "qr_code")
    private String qrCode;

    /**
     * 疫苗相关数据
     */
    @TableField(value = "vaccine")
    private String vaccine;

    /**
     * 核酸检测相关信息
     */
    @TableField(value = "nucleic")
    private String nucleic;

    /**
     * 行程码相关信息
     */
    @TableField(value = "journey")
    private String journey;

    /**
     * 用户名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 用户id
     */
    @TableField(value = "person_id")
    private Long personId;

    /**
     * 门禁id
     */
    @TableField(value = "entrance_guard_id")
    private Long entranceGuardId;


    private static final long serialVersionUID = 1L;
}