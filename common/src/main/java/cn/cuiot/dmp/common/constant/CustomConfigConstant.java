package cn.cuiot.dmp.common.constant;

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
    public static final List<String> HOUSES_ARCHIVES_INIT = Lists.newArrayList("房屋户型", "房屋朝向", "房屋物业业态",
            "房屋状态", "房屋用途", "房屋经营性质", "房屋资源类型", "房屋产权属性", "房屋停车区域", "房屋基础服务");
    public static final List<String> ROOM_ARCHIVES_INIT = Lists.newArrayList("空间分类", "空间专业用途", "空间经营性质",
            "空间产权属性", "空间资源类型", "空间物业服务档次", "空间定位方式");
    public static final List<String> DEVICE_ARCHIVES_INIT = Lists.newArrayList("设备类别", "设备状态", "设备物业服务档次");
    public static final List<String> PARKING_ARCHIVES_INIT = Lists.newArrayList("车位所属区域", "车位使用情况", "车位类型");
    public static final List<String> CUSTOMER_INFO_INIT = Lists.newArrayList("客户分类", "客户类型", "客户级别",
            "公司性质", "信用等级", "所属行业", "证件类型");
    public static final List<String> WORK_OPTION_INIT = Lists.newArrayList("挂起原因", "终止原因", "退回原因",
            "撤回原因", "处理节点类型");
    public static final List<String> CLUE_OPTION_INIT = Lists.newArrayList("线索来源", "跟进状态", "线索结果");

}
