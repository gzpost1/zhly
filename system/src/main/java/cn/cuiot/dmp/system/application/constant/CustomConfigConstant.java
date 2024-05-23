package cn.cuiot.dmp.system.application.constant;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/22
 */
public class CustomConfigConstant {

    /**
     * 自定义配置相关
     */
    public static final List<String> HOUSES_ARCHIVES_INIT = Lists.newArrayList("房屋户型", "朝向", "物业业态",
            "状态", "用途", "经营性质", "资源类型", "产权属性", "停车区域", "基础服务");
    public static final List<String> ROOM_ARCHIVES_INIT = Lists.newArrayList("空间分类", "专业用途", "经营性质",
            "产权属性", "资源类型", "物业服务档次", "定位方式");
    public static final List<String> DEVICE_ARCHIVES_INIT = Lists.newArrayList("设备类别", "物业服务档次");
    public static final List<String> PARKING_ARCHIVES_INIT = Lists.newArrayList("所属区域", "使用情况", "车位类型");

}
