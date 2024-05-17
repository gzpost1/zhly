package cn.cuiot.dmp.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 分页查询
 *
 * @author: wuyongchong
 * @date: 2024/5/6 10:09
 */

/**
 * 分页
 */
@Data
public class PageQuery implements Serializable {

    private final static Long DEFAULT_PAGE_SIZE = 10L;
    private final static Long MAX_PAGE_SIZE = Long.MAX_VALUE;

    /**
     * 当前页
     */
    private Long pageNo = 1L;

    /**
     * 每页大小
     */
    private Long pageSize = 10L;

    /* 升序字段 */
    private String[] ascs = null;

    /* 降序字段 */
    private String[] descs = null;

    public void setOrderByItems(List<OrderItemParam> orderByItems) {
        if (CollectionUtils.isEmpty(orderByItems)) {
            this.orderByItems = orderByItems;
            return;
        }
        orderByItems.forEach(item -> {
            if (!StringUtils.isEmpty(item.getColumn()) && !item.getColumn().toLowerCase().trim()
                    .equals(cleanOrderBy(item.getColumn()).toLowerCase().trim())) {
                throw new RuntimeException("您发送请求中的参数中含有非法字符");
            }
        });
        this.orderByItems = orderByItems;
    }

    /**
     * 排序参数 orderItem.column 字段名称 orderItem.asc 是否升序
     */
    private List<OrderItemParam> orderByItems;

    public Long getPageNo() {
        if (pageNo < 1) {
            pageNo = 1L;
        }
        return pageNo;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public Long getPageSize() {
        if (pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public String[] getAscs() {
        return ascs;
    }

    public void setAscs(String[] ascs) {
        if (ascs == null || ascs.length <= 0) {
            this.ascs = ascs;
            return;
        }
        Arrays.stream(ascs).forEach(ascs1 -> {
            if (!StringUtils.isEmpty(ascs1) && !ascs1.toLowerCase().trim()
                    .equals(cleanOrderBy(ascs1).toLowerCase().trim())) {
                throw new RuntimeException("您发送请求中的参数中含有非法字符");
            }
        });
        this.ascs = ascs;
    }

    public String[] getDescs() {
        return descs;
    }

    public void setDescs(String[] descs) {
        if (descs == null || descs.length <= 0) {
            this.descs = descs;
            return;
        }
        Arrays.stream(descs).forEach(des -> {
            if (!StringUtils.isEmpty(des) && !des.toLowerCase().trim()
                    .equals(cleanOrderBy(des).toLowerCase().trim())) {
                throw new RuntimeException("您发送请求中的参数中含有非法字符");
            }
        });
        this.descs = descs;
    }

    public void initOrderBy() {
        List<OrderItemParam> orderItems = new ArrayList<>();
        orderItems.add(OrderItemParam.asc("sort"));
        orderItems.add(OrderItemParam.desc("update_time"));
        orderItems.add(OrderItemParam.desc("create_time"));
        if (this.getOrderByItems() == null || this.getOrderByItems().size() <= 0) {
            this.setOrderByItems(orderItems);
        }
    }

    //效验
    protected static boolean sqlValidate(String str) {
        str = str.toLowerCase();//统一转为小写
        String badStr =
                "'|sleep|exec |execute |insert |select |delete |update |count(|drop |*|%|chr(|mid" +
                        "(|master|truncate |" +
                        "char(|declare|sitename|net user|xp_cmdshell|;|or |-|+|,|like'|and |exec |insert |create |drop |"
                        +
                        "table |from |grant |use |group_concat|column_name|" +
                        "information_schema.columns|table_schema|union |where |order |by |*|" +
                        ";|-|--|+|,|//|/|%|#";//过滤掉的sql关键字，可以手动添加
        String[] badStrs = badStr.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if (str.indexOf(badStrs[i]) >= 0) {
                return true;
            }
        }
        return false;
    }

    private String cleanOrderBy(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        //统一转为小写
        str = str.toLowerCase();
        String badStr =
                "^'|if\\(|sleep|exec |execute |insert |select |delete |update |count\\(|drop |\\*|%|chr\\"
                        +
                        "(|mid\\(|master|truncate |" +
                        "char\\(|declare|sitename|net user|xp_cmdshell|;|or |\\+|like'|and |exec |insert |create |drop |"
                        +
                        "table |from |grant |use |group_concat|column_name|" +
                        "information_schema.columns|table_schema|union |where |order |by |\\*|" +
                        "--|//|/|#|load_file$";
        Pattern p1 = Pattern.compile(badStr);
        Matcher m1 = p1.matcher(str);
        return m1.replaceAll("").trim();
    }
}