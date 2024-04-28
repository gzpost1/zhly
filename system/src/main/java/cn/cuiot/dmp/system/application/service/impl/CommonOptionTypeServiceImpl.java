package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.system.application.param.vo.CommonOptionTypeVO;
import cn.cuiot.dmp.system.application.service.CommonOptionTypeService;
import cn.cuiot.dmp.system.domain.aggregate.CommonOptionType;
import cn.cuiot.dmp.system.domain.repository.CommonOptionTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author caorui
 * @date 2024/4/26
 */
@Slf4j
@Service
public class CommonOptionTypeServiceImpl implements CommonOptionTypeService {

    @Autowired
    private CommonOptionTypeRepository commonOptionTypeRepository;

    @Override
    public CommonOptionTypeVO queryForDetail(Long id) {
        CommonOptionType commonOptionType = commonOptionTypeRepository.queryForDetail(id);
        CommonOptionTypeVO commonOptionTypeVO = new CommonOptionTypeVO();
        BeanUtils.copyProperties(commonOptionType, commonOptionTypeVO);
        return commonOptionTypeVO;
    }

}
