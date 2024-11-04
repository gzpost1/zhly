package cn.cuiot.dmp.externalapi.service.mapper.gw.entranceguard;

import cn.cuiot.dmp.externalapi.service.entity.gw.entranceguard.GwEntranceGuardPersonEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 联通格物门禁-人员管理 mapper接口
 *
 * @Author: zc
 * @Date: 2024-09-07
 */
public interface GwEntranceGuardPersonMapper extends BaseMapper<GwEntranceGuardPersonEntity> {

    /**
     * 根据ic卡查询数据
     *
     * @param icCardNo ic卡号
     * @param id       数据id
     * @return Boolean
     */
    Boolean isExistsIcCardNo(@Param("icCardNo") String icCardNo, @Param("id") Long id);

    /**
     * 查询详情
     *
     * @param companyId 企业id
     * @param id       数据id
     * @return Boolean
     */
    GwEntranceGuardPersonEntity queryForDetail(@Param("companyId") Long companyId, @Param("id") Long id);

    /**
     * 列表
     *
     * @param companyId 企业id
     * @param ids       数据id列表
     * @return Boolean
     */
    List<GwEntranceGuardPersonEntity> queryForListByIds(@Param("companyId") Long companyId, @Param("ids") List<Long> ids);
}
