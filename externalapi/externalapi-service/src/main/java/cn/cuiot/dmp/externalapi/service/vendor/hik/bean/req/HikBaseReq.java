package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req;

import lombok.Data;

/**
 * 基础分页请求
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Data
public class HikBaseReq {

    /**
     * 当前页码
     */
    private Integer pageNo;

    /**
     * 当前页码
     */
    private Integer pageSize;
}
