package cn.cuiot.dmp.common.enums;

import cn.cuiot.dmp.common.bean.external.SDKDWaterMeterBO;
import cn.cuiot.dmp.common.bean.external.VsuapVideoBO;
import cn.cuiot.dmp.common.bean.external.YFEntranceGuardBO;
import cn.cuiot.dmp.common.bean.external.YFPortraitInputBO;
import cn.cuiot.dmp.common.utils.JsonUtil;

import java.util.Objects;

/**
 * 外部对接平台枚举
 *
 * @Author: zc
 * @Date: 2024-08-21
 */
public enum FootPlateInfoEnum {

    /**
     * 大华宇泛-人脸录像
     */
    YF_PORTRAIT_INPUT(1L, YFPortraitInputBO.class),

    /**
     * 联通-云智眼
     */
    VSUAP_VIDEO(2L, VsuapVideoBO.class),

    /**
     * 山东科德-物联网水表
     */
    SDKD_WATER_METER(3L, SDKDWaterMeterBO.class),

    /**
     * 门禁（宇泛）
     */
    YF_ENTRANCE_GUARD(4L, YFEntranceGuardBO.class),

    ;

    private Long id;
    private Class<?> clazz;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    FootPlateInfoEnum(Long id, Class<?> clazz) {
        this.id = id;
        this.clazz = clazz;
    }

    /**
     * 通过id找到对应的枚举，并将json转换为该枚举指定的实体类
     * @param id 枚举的id
     * @param jsonData 要转换的json数据
     * @return 枚举对应的实体类对象
     * @throws IllegalArgumentException 如果未找到对应id的枚举
     */
    public static <T> T getObjectFromJsonById(Long id, String jsonData) {
        for (FootPlateInfoEnum value : FootPlateInfoEnum.values()) {
            if (Objects.equals(value.getId(), id)) {
                // 使用枚举的 clazz 进行类型转换
                return (T)JsonUtil.readValue(jsonData, value.clazz);
            }
        }
        throw new IllegalArgumentException("No enum found for id: " + id);
    }
}
