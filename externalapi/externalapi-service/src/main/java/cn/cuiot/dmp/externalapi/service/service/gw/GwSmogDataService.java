package cn.cuiot.dmp.externalapi.service.service.gw;

import cn.cuiot.dmp.externalapi.service.entity.gw.GwSmogDataEntity;
import cn.cuiot.dmp.externalapi.service.mapper.gw.GwSmogDataMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 格物烟雾报警器设备属性 服务类
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-23
 */
@Service
public class GwSmogDataService extends ServiceImpl<GwSmogDataMapper, GwSmogDataEntity> {

    /**
     * 查询最新的属性数据
     * @return
     */
    public GwSmogDataEntity queryLatestData(Long deviceId){
        return baseMapper.queryLatestData(deviceId);
    }



}
