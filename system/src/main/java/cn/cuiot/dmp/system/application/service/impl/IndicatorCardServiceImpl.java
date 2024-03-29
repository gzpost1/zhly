package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.system.application.constant.CurrencyConst;
import cn.cuiot.dmp.system.application.enums.DepartmentGroupEnum;
import cn.cuiot.dmp.system.application.service.IndicatorCardService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.DepartmentDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.IndicatorCardListPo;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.DepartmentDao;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.IndicatorCardDao;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zjb
 * @classname IndicatorCardServiceImpl
 * @description 描述
 * @date 2023/1/13
 */
@Slf4j
@Service
public class IndicatorCardServiceImpl implements IndicatorCardService {

    @Autowired
    private IndicatorCardDao indicatorCardDao;

    @Autowired
    private DepartmentDao departmentDao;

    /**
     * 厂园区指标卡
     *
     * @param orgId
     * @param userId
     * @return
     */
    @Override
    public Map<String, Integer> factoryPark(String orgId, String userId) {

        // 获取用户所在组织
        DepartmentDto userDept = departmentDao.getPathByUser(userId);
        String userPath = userDept.getPath();
        // 获取标签卡
        List<IndicatorCardListPo> list = indicatorCardDao.factoryPark(userPath);
        Map<String, Integer> map = list.stream().collect(Collectors.toMap(IndicatorCardListPo::getName, IndicatorCardListPo::getNumber));
        // 判断用户所处组织层级，场所级用户的上级场所数量默认为：1 ， 是自身所在场所
        int dGroup = Integer.parseInt(userDept.getDGroup());
        if (DepartmentGroupEnum.COMMUNITY.getCode().equals(dGroup)) {
            map.put(CurrencyConst.PARK, CurrencyConst.INT_ONE);
        } else if (DepartmentGroupEnum.REGION.getCode().equals(dGroup)) {
            map.put(CurrencyConst.PARK, CurrencyConst.INT_ONE);
            map.put(CurrencyConst.REGION, CurrencyConst.INT_ONE);
        } else if (DepartmentGroupEnum.BUILDING.getCode().equals(dGroup)) {
            map.put(CurrencyConst.PARK, CurrencyConst.INT_ONE);
            map.put(CurrencyConst.REGION, CurrencyConst.INT_ONE);
            map.put(CurrencyConst.BUILDING, CurrencyConst.INT_ONE);
        } else if (DepartmentGroupEnum.FLOOR.getCode().equals(dGroup)) {
            map.put(CurrencyConst.PARK, CurrencyConst.INT_ONE);
            map.put(CurrencyConst.REGION, CurrencyConst.INT_ONE);
            map.put(CurrencyConst.BUILDING, CurrencyConst.INT_ONE);
            map.put(CurrencyConst.FLOOR, CurrencyConst.INT_ONE);
        }
        return map;
    }

}
