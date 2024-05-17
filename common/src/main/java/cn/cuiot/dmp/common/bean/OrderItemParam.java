package cn.cuiot.dmp.common.bean;

import java.io.Serializable;
import lombok.Data;

/**
 * 排序参数
 *
 * @author: wuyongchong
 * @date: 2024/5/6 10:10
 */
@Data
public class OrderItemParam implements Serializable {

    /**
     * 字段
     */
    private String column;
    /**
     * 是否什序
     */
    private boolean asc = true;

    public static OrderItemParam asc(String column) {
        return build(column, true);
    }

    public static OrderItemParam desc(String column) {
        return build(column, false);
    }

    private static OrderItemParam build(String column, boolean asc) {
        OrderItemParam item = new OrderItemParam();
        item.setColumn(column);
        item.setAsc(asc);
        return item;
    }

}
