package cn.cuiot.dmp.system.infrastructure.persistence.dao;

import cn.cuiot.dmp.system.infrastructure.entity.HouseEntity;
import cn.cuiot.dmp.system.infrastructure.entity.HousePropertyEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PropertyHouseInfoResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PropertyHouseListReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.PropertyHouseListReqVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @param
 * @Author xieSH
 * @Description houseDao
 * @Date 2021/12/28 15:08
 * @return
 **/
@Mapper
public interface HouseDao {

    /**
     * 新增
     * @param houseEntity
     * @return
     */
    int insert(HouseEntity houseEntity);


    /**
     * 物业侧新增房屋
     * @param housePropertyEntity
     * @return
     */
    int insertPropertyHouse(HousePropertyEntity housePropertyEntity);

    /**
     * 更新
     * @param houseEntity
     * @return
     */
    int update(HouseEntity houseEntity);

    /**
     * 物业侧房屋更新
     * @param housePropertyEntity
     * @return
     */
    int updatePropertyHouse(HousePropertyEntity housePropertyEntity);

    /**
     * 根据条件查询全部房屋信息
     * @param dto
     * @return
     */
    List<PropertyHouseListReqVO> selectPropertyHouseList(PropertyHouseListReqDto dto);

    /**
     * 根据主键id查询
     * @param id
     * @return
     */
    HouseEntity selectByPrimary(Long id);

    /**
     * 根据主键id查询物业侧房屋
     * @param id
     * @return
     */
    PropertyHouseInfoResDto selectPropertyByPrimary(@Param("id") Long id, @Param("path") String path);

    /**
     * 批量删除
     *
     * @param ids    主键idList
     * @param userId 删除人id
     * @return
     */
    int batchDelete(@Param("ids") List<Long> ids, @Param("userId") String userId);

    /**
     * 查询是否存在
     * @param entity
     * @return
     */
    int selectPropertyHouseNameExist(HousePropertyEntity entity);

    /**
     * 通过房屋id获取deptId
     * @param houseId
     * @return
     */
    String getDeptIdByHouseId(@Param("houseId") Long houseId);

}
