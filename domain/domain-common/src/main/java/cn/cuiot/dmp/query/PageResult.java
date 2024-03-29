package cn.cuiot.dmp.query;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    /**
     * 总条数
     */
    private long total;

    /**
     * 列表
     */
    private List<T> list;

    /**
     * 分页参数，当前页面显示的数据条数
     */
    private int pageSize;

    /**
     * 分页参数，当前页码
     */
    private int currentPage;
}
