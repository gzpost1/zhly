package cn.cuiot.dmp.common.constant;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static final List<String> NOTICE_TYPE_INIT = Lists.newArrayList("公告类型");
    public static final List<String> CONTRACT_INTENTION_INIT = Lists.newArrayList("退定原因", "意向金收费项目");
    public static final List<String> CONTRACT_LEASE_INIT = Lists.newArrayList("租赁用途", "退租类型", "退租原因");
    public static final List<String> CONTRACT_TEMPLATE_INIT = Lists.newArrayList("合同类型", "合同性质");
    public static final List<String> PRICE_MANAGE_INIT = Lists.newArrayList("定价单类别", "定价单类型");
    public static final Long DEFAULT_USER_ID = 1L;

    /**
     * 自定义配置选项值相关
     */
    public static final List<String> WITHDRAWAL_REASON_DETAIL_INIT = Lists.newArrayList("计划变更-0");
    public static final List<String> INTENTION_MONEY_DETAIL_INIT = Lists.newArrayList("租赁保证金-0");
    public static final List<String> LEASE_USE_DETAIL_INIT = Lists.newArrayList("商业-0", "办公-1");
    public static final List<String> LEASE_TYPE_DETAIL_INIT = Lists.newArrayList("到期不续租-0", "提前退租-1",
            "私自搬走、逃铺-2", "正常到期退租-3");
    public static final List<String> LEASE_REASON_DETAIL_INIT = Lists.newArrayList("卫生环境-0", "交通不便-1",
            "正常到期退租-2", "物业服务-3", "设施配置-4", "公司扩张-5", "经营不善-6", "价格因素-7");
    public static final List<String> CONTRACT_TYPE_DETAIL_INIT = Lists.newArrayList("默认类型-0");
    public static final List<String> CONTRACT_NATURE_DETAIL_INIT = Lists.newArrayList("框架协议-0", "定价合同-1");
    public static final List<String> PRICE_CATEGORY_DETAIL_INIT = Lists.newArrayList("内部定价-0", "外部定价-1");
    public static final List<String> PRICE_TYPE_DETAIL_INIT = Lists.newArrayList("元/月-0", "元/天-1", "元/㎡*月-2",
            "元/㎡*天-3", "元/付费周期-4");

    public static final Map<Long, List<String>> CONTRACT_DETAIL_MAP_INIT = new HashMap<Long, List<String>>() {
        private static final long serialVersionUID = 1634977214610442295L;

        {
            put(1793210915354824824L, WITHDRAWAL_REASON_DETAIL_INIT);
            put(1793210915354824825L, INTENTION_MONEY_DETAIL_INIT);
            put(1793210915354824826L, LEASE_USE_DETAIL_INIT);
            put(1793210915354824827L, LEASE_TYPE_DETAIL_INIT);
            put(1793210915354824828L, LEASE_REASON_DETAIL_INIT);
            put(1793210915354824829L, CONTRACT_TYPE_DETAIL_INIT);
            put(1793210915354824830L, CONTRACT_NATURE_DETAIL_INIT);
            put(1793210915354824831L, PRICE_CATEGORY_DETAIL_INIT);
            put(1793210915354824832L, PRICE_TYPE_DETAIL_INIT);
        }
    };

    public static final Long FIRST_CONTRACT_CUSTOM_CONFIG_ID = 1793210915354824824L;

}
