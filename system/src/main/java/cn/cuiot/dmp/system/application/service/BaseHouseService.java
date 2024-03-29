package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.infrastructure.entity.dto.HouseDeleteDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.CommercialComplexHouseDetailResVO;

/**
 * @Author: huw51
 * @Description: 房屋service
 * @Date: create in 2023/2/13 11:36
 */
public interface BaseHouseService {

    /**
     * 通过商铺主键id获取商铺详情
     * @param id
     * @return
     */
    CommercialComplexHouseDetailResVO detail(Long id, String orgId, String userId);

    /**
     * 批量删除商铺
     * @param deleteDto
     * @return
     */
    int batchDelete(HouseDeleteDto deleteDto);

}
