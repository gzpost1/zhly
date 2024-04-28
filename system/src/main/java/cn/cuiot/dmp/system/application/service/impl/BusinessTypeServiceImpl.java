package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.system.application.constant.BusinessTypeConstant;
import cn.cuiot.dmp.system.application.param.dto.BusinessTypeCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.BusinessTypeUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.BusinessTypeVO;
import cn.cuiot.dmp.system.application.service.BusinessTypeService;
import cn.cuiot.dmp.system.domain.aggregate.BusinessType;
import cn.cuiot.dmp.system.domain.repository.BusinessTypeRepository;
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
public class BusinessTypeServiceImpl implements BusinessTypeService {

    @Autowired
    private BusinessTypeRepository businessTypeRepository;

    @Override
    public BusinessTypeVO queryForDetail(Long id) {
        BusinessType businessType = businessTypeRepository.queryForDetail(id);
        BusinessTypeVO businessTypeVO = new BusinessTypeVO();
        BeanUtils.copyProperties(businessType, businessTypeVO);
        return businessTypeVO;
    }

    @Override
    public int saveBusinessType(BusinessTypeCreateDTO businessTypeCreateDTO) {
        AssertUtil.isTrue(BusinessTypeConstant.MAX_LEVEL_TYPE >= businessTypeCreateDTO.getLevelType(),
                "业务类型超过最大层级");
        BusinessType businessType = new BusinessType();
        BeanUtils.copyProperties(businessTypeCreateDTO, businessType);
        return businessTypeRepository.saveBusinessType(businessType);
    }

    @Override
    public int updateBusinessType(BusinessTypeUpdateDTO businessTypeUpdateDTO) {
        AssertUtil.isTrue(BusinessTypeConstant.MAX_LEVEL_TYPE >= businessTypeUpdateDTO.getLevelType(),
                "业务类型超过最大层级");
        BusinessType businessType = businessTypeRepository.queryForDetail(businessTypeUpdateDTO.getId());
        BeanUtils.copyProperties(businessTypeUpdateDTO, businessType);
        return businessTypeRepository.updateBusinessType(businessType);
    }

}
