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

    private List<T> records;

    private long total;
    /**
     * 每页数量
     */
    private int pageSize;
    /**
     * 每页数量
     */
    private int size;

    /**
     * 当前页
     */
    private int currentPage;

    /**
     * 当前页
     */
    private int current;


    private int pages;

    /**
     * 当前页
     */
    private int pageNo;

    public PageResult() {
    }

    public PageResult(Page page) {
        if (page != null) {
            pageSize = page.getPageSize();
            size = page.getPageSize();
            currentPage = page.getPageNum();
            current = page.getPageNum();
            pageNo = page.getPageNum();
            pages = page.getPages();
            total = page.getTotal();
            list = page.getResult();
        }

    }

    public PageResult(Page page, List datas) {
        if (page != null) {
            pageSize = page.getPageSize();
            size = page.getPageSize();
            currentPage = page.getPageNum();
            current = page.getPageNum();
            pageNo = page.getPageNum();
            pages = page.getPages();
            total = page.getTotal();
            this.list = datas;
        }

    }

    public PageResult(PageInfo pageInfo) {
        if (pageInfo != null) {
            pageSize = pageInfo.getPageSize();
            size = pageInfo.getPageSize();
            currentPage = pageInfo.getPageNum();
            current = pageInfo.getPageNum();
            pageNo = pageInfo.getPageNum();
            pages = pageInfo.getPages();
            total = pageInfo.getTotal();
            this.list = pageInfo.getList();
        }

    }

    public List<T> getRecords() {
        return list;
    }
}
