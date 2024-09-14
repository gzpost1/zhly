package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zc
 * @Date: 2024-09-06
 */
@Data
public class BaseDmpPageResp implements Serializable {

    private static final long serialVersionUID = 7571654519652604182L;

    /**
     * 当前页
     */
    private Integer currentPage;

    /**
     * 总条数
     */
    private Integer total;

}
