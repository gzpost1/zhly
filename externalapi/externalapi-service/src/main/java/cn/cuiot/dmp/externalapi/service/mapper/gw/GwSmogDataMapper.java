package cn.cuiot.dmp.externalapi.service.mapper.gw;


import cn.cuiot.dmp.externalapi.service.entity.gw.GwSmogDataEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 格物烟雾报警器设备属性 Mapper 接口
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-23
 */
public interface GwSmogDataMapper extends BaseMapper<GwSmogDataEntity> {

    /**
     * 查询最新的属性数据
     * @return
     */
    GwSmogDataEntity queryLatestData(@Param("deviceId")Long deviceId);

    /**
     * 只保留一部分属性数据
     * @param deviceId
     * @return
     */
    Integer deleteSmogDataByLimit(@Param("deviceId")Long deviceId);
}
