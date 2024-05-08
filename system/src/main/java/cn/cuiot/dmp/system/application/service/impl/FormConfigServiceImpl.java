package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.system.application.param.dto.BatchFormConfigDTO;
import cn.cuiot.dmp.system.application.param.dto.FormConfigCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.FormConfigUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.FormConfigVO;
import cn.cuiot.dmp.system.application.service.FormConfigService;
import cn.cuiot.dmp.system.domain.aggregate.FormConfig;
import cn.cuiot.dmp.system.domain.repository.FormConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author caorui
 * @date 2024/4/26
 */
@Slf4j
@Service
public class FormConfigServiceImpl implements FormConfigService {

    @Autowired
    private FormConfigRepository formConfigRepository;

    @Override
    public FormConfigVO queryForDetail(Long id) {
        FormConfig formConfig = formConfigRepository.queryForDetail(id);
        FormConfigVO formConfigVO = new FormConfigVO();
        BeanUtils.copyProperties(formConfig, formConfigVO);
        return formConfigVO;
    }

    @Override
    public int saveFormConfig(FormConfigCreateDTO createDTO) {
        FormConfig formConfig = new FormConfig();
        BeanUtils.copyProperties(createDTO, formConfig);
        return formConfigRepository.saveFormConfig(formConfig);
    }

    @Override
    public int updateFormConfig(FormConfigUpdateDTO updateDTO) {
        FormConfig formConfig = new FormConfig();
        BeanUtils.copyProperties(updateDTO, formConfig);
        return formConfigRepository.updateFormConfig(formConfig);
    }

    @Override
    public int updateFormConfigStatus(UpdateStatusParam updateStatusParam) {
        FormConfig formConfig = new FormConfig();
        BeanUtils.copyProperties(updateStatusParam, formConfig);
        return formConfigRepository.updateFormConfigStatus(formConfig);
    }

    @Override
    public int deleteFormConfig(Long id) {
        return formConfigRepository.deleteFormConfig(id);
    }

    @Override
    public int batchMoveFormConfig(BatchFormConfigDTO batchFormConfigDTO) {
        Long typeId = batchFormConfigDTO.getTypeId();
        List<Long> idList = batchFormConfigDTO.getIdList();
        return formConfigRepository.batchMoveFormConfig(typeId, idList);
    }

    @Override
    public int batchUpdateFormConfigStatus(BatchFormConfigDTO batchFormConfigDTO) {
        Byte status = batchFormConfigDTO.getStatus();
        List<Long> idList = batchFormConfigDTO.getIdList();
        return formConfigRepository.batchUpdateFormConfigStatus(status, idList);
    }

    @Override
    public int batchDeleteFormConfig(List<Long> idList) {
        return formConfigRepository.batchDeleteFormConfig(idList);
    }

}
