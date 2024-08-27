package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.req.PlatfromInfoReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.system.infrastructure.entity.PlatfromInfoEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.PlatfromInfoMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
}
