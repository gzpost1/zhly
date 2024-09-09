package cn.cuiot.dmp.externalapi.service.mapper.gw;

import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardPersonEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 联通格物门禁-人员管理 mapper接口
 *
 * @Author: zc
 * @Date: 2024-09-07
 */
public interface GwEntranceGuardPersonMapper extends BaseMapper<GwEntranceGuardPersonEntity> {

    /**
     * 根据ic卡查询数据
     */
    Boolean isExistsIcCardNo(@Param("icCardNo") String icCardNo, @Param("id") Long id);
}
