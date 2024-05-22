package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.application.param.dto.CustomConfigCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.CustomConfigUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.CustomConfigVO;
import cn.cuiot.dmp.system.application.service.CustomConfigService;
import cn.cuiot.dmp.system.domain.aggregate.CustomConfig;
import cn.cuiot.dmp.system.domain.aggregate.CustomConfigPageQuery;
import cn.cuiot.dmp.system.domain.repository.CustomConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/5/19
 */
@Slf4j
@Service
public class CustomConfigServiceImpl implements CustomConfigService {
    
    @Autowired
    private CustomConfigRepository customConfigRepository;
    
    @Override
    public CustomConfigVO queryForDetail(Long id) {
        CustomConfig customConfig = customConfigRepository.queryForDetail(id);
        CustomConfigVO customConfigVO = new CustomConfigVO();
        BeanUtils.copyProperties(customConfig, customConfigVO);
        return customConfigVO;
    }

    @Override
    public int saveCustomConfig(CustomConfigCreateDTO createDTO) {
        CustomConfig customConfig = new CustomConfig();
        BeanUtils.copyProperties(createDTO, customConfig);
        return customConfigRepository.saveCustomConfig(customConfig);
    }

    @Override
    public int updateCustomConfig(CustomConfigUpdateDTO updateDTO) {
        CustomConfig customConfig = new CustomConfig();
        BeanUtils.copyProperties(updateDTO, customConfig);
        return customConfigRepository.updateCustomConfig(customConfig);
    }

    @Override
    public int updateCustomConfigStatus(UpdateStatusParam updateStatusParam) {
        CustomConfig customConfig = new CustomConfig();
        BeanUtils.copyProperties(updateStatusParam, customConfig);
        return customConfigRepository.updateCustomConfigStatus(customConfig);
    }

    @Override
    public int deleteCustomConfig(Long id) {
        return customConfigRepository.deleteCustomConfig(id);
    }

    @Override
    public PageResult<CustomConfigVO> queryCustomConfigByType(CustomConfigPageQuery pageQuery) {
        PageResult<CustomConfig> customConfigPageResult = customConfigRepository.queryCustomConfigByType(pageQuery);
        if (CollectionUtils.isEmpty(customConfigPageResult.getList())) {
            return new PageResult<>();
        }
        PageResult<CustomConfigVO> CustomConfigVOPageResult = new PageResult<>();
        List<CustomConfigVO> CustomConfigVOList = customConfigPageResult.getList().stream()
                .map(o -> {
                    CustomConfigVO customConfigVO = new CustomConfigVO();
                    BeanUtils.copyProperties(o, customConfigVO);
                    return customConfigVO;
                }).collect(Collectors.toList());
        BeanUtils.copyProperties(customConfigPageResult, CustomConfigVOPageResult);
        CustomConfigVOPageResult.setList(CustomConfigVOList);
        return CustomConfigVOPageResult;
    }
    
}