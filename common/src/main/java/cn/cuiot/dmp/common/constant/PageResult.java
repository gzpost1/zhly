package cn.cuiot.dmp.common.constant;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * @描述:
 * @Author: sunking
 * @Date: 2020/7/20 12:43 PM
 */
@Data
public class PageResult<T> implements Serializable {

    private List<T> list;

    private long total;
    /**
     * 每页数量
     */
    private int pageSize;
    /**
     * 当前页
     */
    private int currentPage;

    /**
     * 当前页
     */
    private int pageNo;

    public PageResult() {
    }

    public PageResult(Page page) {
        if (page != null) {
            pageSize = page.getPageSize();
            currentPage = page.getPageNum();
            pageNo = page.getPageNum();
            total = page.getTotal();
            list = page.getResult();
        }

    }

    public PageResult(Page page, List datas) {
        if (page != null) {
            pageSize = page.getPageSize();
            currentPage = page.getPageNum();
            pageNo = page.getPageNum();
            total = page.getTotal();
            this.list = datas;
        }

    }

    public PageResult(PageInfo pageInfo) {
        if (pageInfo != null) {
            pageSize = pageInfo.getPageSize();
            currentPage = pageInfo.getPageNum();
            pageNo = pageInfo.getPageNum();
            total = pageInfo.getTotal();
            list = pageInfo.getList();
        }

    }

}
