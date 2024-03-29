package cn.cuiot.dmp.system.application.service;

import java.util.Map;

/**
 * @author zjb
 * @classname IndicatorCardService
 * @description 指标卡业务
 * @date 2023/1/13
 */
public interface IndicatorCardService {

    /**
     * 厂园区指标卡
     *
     * @param orgId
     * @param userId
     * @return
     */
    Map<String, Integer> factoryPark(String orgId, String userId);

}
