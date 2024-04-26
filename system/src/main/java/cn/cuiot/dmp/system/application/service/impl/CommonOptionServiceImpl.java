package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.system.application.param.vo.CommonOptionVO;
import cn.cuiot.dmp.system.application.service.CommonOptionService;
import cn.cuiot.dmp.system.domain.aggregate.CommonOption;
import cn.cuiot.dmp.system.domain.repository.CommonOptionRepository;
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
public class CommonOptionServiceImpl implements CommonOptionService {

    @Autowired
    private CommonOptionRepository commonOptionRepository;

    @Override
    public CommonOptionVO queryForDetail(Long id) {
        CommonOption commonOption = commonOptionRepository.queryForDetail(id);
        CommonOptionVO commonOptionVO = new CommonOptionVO();
        BeanUtils.copyProperties(commonOption, commonOptionVO);
        return commonOptionVO;
    }
}
