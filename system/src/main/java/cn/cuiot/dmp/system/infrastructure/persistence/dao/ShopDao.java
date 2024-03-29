package cn.cuiot.dmp.system.infrastructure.persistence.dao;

import cn.cuiot.dmp.system.infrastructure.entity.dto.PropertyHouseListReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.po.HousePo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: huw51
 * @Description: 商铺dao
 * @Date: create in 2023/2/13 14:20
 */
@Mapper
public interface ShopDao {

    /**
     * 获取商业综合体商铺
     * @param propertyHouseListReqDto
     * @return
     */
    List<HousePo> selectCommercialComplexShopList(PropertyHouseListReqDto propertyHouseListReqDto);

    /**
     * 商业综合体-详情
     * @param id
     * @return
     */
    HousePo detail(@Param("id") Long id, @Param("path") String path);

    /**
     * 批量删除商铺
     * @param ids
     * @param userId
     * @return
     */
    int batchDelete(@Param("ids") List<Long> ids, @Param("userId") String userId);

}
