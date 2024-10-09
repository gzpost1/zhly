package cn.cuiot.dmp.externalapi.service.mapper.park;

import cn.cuiot.dmp.externalapi.service.entity.park.ParkInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author pengjian
 * @since 2024-09-03
 */
public interface ParkInfoMapper extends BaseMapper<ParkInfoEntity> {


    void insertOrUpdateBatch(@Param("parkList") List<ParkInfoEntity> parkList);

    List<Integer> queryParkIds();
}
