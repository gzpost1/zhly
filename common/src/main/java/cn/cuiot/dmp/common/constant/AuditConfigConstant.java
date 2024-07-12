package cn.cuiot.dmp.common.constant;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author caorui
 * @date 2024/6/11
 */
public class AuditConfigConstant {

    /**
     * 审核配置相关
     */
    public static final List<String> NOTICE_MANAGE_INIT = Lists.newArrayList("新增公告", "编辑公告");
    public static final List<String> CONTENT_MANAGE_INIT = Lists.newArrayList("新增图文", "编辑图文");
    public static final List<String> PRICE_MANAGE_INIT = Lists.newArrayList("定价单提交");
    public static final List<String> LEASE_CONTRACT_INIT = Lists.newArrayList("合同新建后提交", "合同退租", "合同变更",
            "合同续租", "合同作废");
    public static final List<String> INTENTION_CONTRACT_INIT = Lists.newArrayList("合同新建后提交", "合同退定",
            "合同作废", "合同签约");
    public static final Long DEFAULT_USER_ID = 1L;

}
