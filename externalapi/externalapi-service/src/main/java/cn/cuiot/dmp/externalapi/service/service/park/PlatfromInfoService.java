package cn.cuiot.dmp.externalapi.service.service.park;

import cn.cuiot.dmp.base.infrastructure.dto.req.PlatfromInfoReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.externalapi.service.entity.park.PlatfromInfoEntity;
import cn.cuiot.dmp.externalapi.service.mapper.park.PlatfromInfoMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author pengjian
 * @since 2024-07-18
 */
@Service
public class PlatfromInfoService extends ServiceImpl<PlatfromInfoMapper, PlatfromInfoEntity>{

    public Page<PlatfromInfoRespDTO> queryForPage(PlatfromInfoReqDTO dto) {
        return baseMapper.queryForPage(new Page<>(dto.getPageNo(), dto.getPageSize()), dto);
    }

    public List<PlatfromInfoRespDTO> queryForList(PlatfromInfoReqDTO dto) {
        return baseMapper.queryForList(dto);
    }
}
