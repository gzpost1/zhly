package cn.cuiot.dmp.externalapi.service.query;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 门禁（宇泛）分页
 *
 * @date 2024/8/21 14:00
 * @author gxp
 */
@Data
public class UniUbiPage<R> implements Serializable {
    private static final long serialVersionUID = -8195569617473037685L;

    /**
     * 总数
     */
    private Integer total;

    /**
     * 页码
     */
    private Integer index;

    /**
     * 每页个数(上限 100)
     */
    private Integer length;

    /**
     * 页数
     */
    private Integer size;

    /**
     * 数据列表
     */
    private List<R> content;

}
