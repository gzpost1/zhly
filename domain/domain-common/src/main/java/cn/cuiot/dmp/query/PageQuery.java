package cn.cuiot.dmp.query;

import java.io.Serializable;
import lombok.Data;

/**
 * @Description 分页查询请求基类
 * @Author 犬豪
 * @Date 2023/9/21 09:05
 * @Version V1.0
 */
@Data
public class PageQuery implements Serializable {
    /**
     * 分页参数，每页数据量
     */
    private Integer pageSize;

    /**
     * 分页参数，当前页
     */
    private Integer currentPage;

    public PageQuery(Integer pageSize, Integer currentPage) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
    }

    public PageQuery() {
    }

    public void init() {
        if (pageSize == null) {
            pageSize = 10;
        }
        if (currentPage == null) {
            currentPage = 1;
        }
    }
}
