package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.FormConfigReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.FormConfigRspDTO;
import cn.cuiot.dmp.common.utils.AssertUtil;
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
import java.util.Objects;
import java.util.stream.Collectors;

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
    public List<FormConfigRspDTO> batchQueryFormConfig(FormConfigReqDTO formConfigReqDTO) {
        AssertUtil.notEmpty(formConfigReqDTO.getIdList(), "表单配置ID列表不能为空");
        Byte status = null;
        if (Objects.nonNull(formConfigReqDTO.getStatus())) {
            status = formConfigReqDTO.getStatus();
        }
        List<Long> idList = formConfigReqDTO.getIdList();
        List<FormConfig> formConfigList = formConfigRepository.batchQueryFormConfig(status, idList);
        return formConfigList.stream()
                .map(o -> {
                    FormConfigRspDTO formConfigRspDTO = new FormConfigRspDTO();
                    BeanUtils.copyProperties(o, formConfigRspDTO);
                    return formConfigRspDTO;
                }).collect(Collectors.toList());
    }

    @Override
    public int batchMoveFormConfig(BatchFormConfigDTO batchFormConfigDTO) {
        AssertUtil.notNull(batchFormConfigDTO.getTypeId(), "表单配置分类不能为空");
        AssertUtil.notEmpty(batchFormConfigDTO.getIdList(), "表单配置ID列表不能为空");
        Long typeId = batchFormConfigDTO.getTypeId();
        List<Long> idList = batchFormConfigDTO.getIdList();
        return formConfigRepository.batchMoveFormConfig(typeId, idList);
    }

    @Override
    public int batchUpdateFormConfigStatus(BatchFormConfigDTO batchFormConfigDTO) {
        AssertUtil.notNull(batchFormConfigDTO.getStatus(), "表单配置状态不能为空");
        AssertUtil.notEmpty(batchFormConfigDTO.getIdList(), "表单配置ID列表不能为空");
        Byte status = batchFormConfigDTO.getStatus();
        List<Long> idList = batchFormConfigDTO.getIdList();
        return formConfigRepository.batchUpdateFormConfigStatus(status, idList);
    }

    @Override
    public int batchDeleteFormConfig(List<Long> idList) {
        return formConfigRepository.batchDeleteFormConfig(idList);
    }

}