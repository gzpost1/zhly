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
    private Long pageNo;

    /**
     * 当前页码
     */
    private Long pageSize;
}
