package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req;

import lombok.Data;

import java.util.List;

/**
 * 根据编号获取区域详细信息query
 *
 * @Author: zc
 * @Date: 2024-10-12
 */
@Data
public class HikRegionDetailReq {

    /**
     * 区域编码
     */
    private List<String> indexCodes;
}
