package cn.cuiot.dmp.externalapi.service.mapper.park;

import cn.cuiot.dmp.base.infrastructure.dto.req.PlatfromInfoReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.externalapi.service.entity.park.PlatfromInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;


/**
 *
 * @author pengjian
 * @since 2024-07-18
 */
public interface PlatfromInfoMapper extends BaseMapper<PlatfromInfoEntity> {

    Page<PlatfromInfoRespDTO> queryForPage(Page page, @Param("params") PlatfromInfoReqDTO dto);
}
