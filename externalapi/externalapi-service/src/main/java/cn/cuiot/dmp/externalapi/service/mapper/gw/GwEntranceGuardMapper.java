package cn.cuiot.dmp.externalapi.service.mapper.gw;

import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardEntity;
import cn.cuiot.dmp.externalapi.service.query.EntranceGuardRecordReqDTO;
import cn.cuiot.dmp.externalapi.service.vo.EntranceGuardRecordVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 格物门禁 mapper接口
 *
 * @Author: zc
 * @Date: 2024-09-06
 */
public interface GwEntranceGuardMapper extends BaseMapper<GwEntranceGuardEntity> {

    GwEntranceGuardEntity queryForDetail(@Param("companyId") Long companyId, @Param("id") Long id);
    List<GwEntranceGuardEntity> queryForListByIds(@Param("companyId") Long companyId, @Param("ids") List<Long> ids);

    /**
     * 查看所有门禁的通行记录
     * @param page
     * @param params
     * @return
     */
    IPage<EntranceGuardRecordVo> entranceGuardQueryForPage(Page page,@Param("params") EntranceGuardRecordReqDTO params);
}