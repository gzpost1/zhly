package cn.cuiot.dmp.system.infrastructure.persistence.dao;

import cn.cuiot.dmp.system.infrastructure.entity.dto.IndicatorCardListPo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author zjb
 * @classname IndicatorCardDao
 * @description 指标卡
 * @date 2023/1/13
 */
@Mapper
public interface IndicatorCardDao {

    /**
     * 厂园区指标卡
     *
     * @param userPath
     * @return
     */
    List<IndicatorCardListPo> factoryPark(@Param("path")String userPath);

}
