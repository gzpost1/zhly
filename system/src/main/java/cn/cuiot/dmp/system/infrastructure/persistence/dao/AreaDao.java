package cn.cuiot.dmp.system.infrastructure.persistence.dao;

import cn.cuiot.dmp.system.infrastructure.entity.AreaEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author wen
 * @Description houseDao
 * @Date 2021/12/28 15:08
 * @param
 * @return
 **/
@Mapper
public interface AreaDao {

    /**
     * 根据code查询areaEntity
     * @param code
     * @return
     */
    AreaEntity selectByCode(String code);

    /**
     * 根据parentCode查询areaEntity
     * @param parentCode
     * @return
     */
    List<AreaEntity> selectByParentCode(@Param("parentCode") String parentCode);

    /**
     * 检查省份code是否存在
     * @param provinceCode
     * @return
     */
    Integer checkProvinceCode(String provinceCode);


    List<AreaEntity> getAllOneProvince();
	
}
