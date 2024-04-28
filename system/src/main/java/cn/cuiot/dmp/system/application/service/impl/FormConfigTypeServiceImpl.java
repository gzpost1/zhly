package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.system.application.param.vo.FormConfigTypeVO;
import cn.cuiot.dmp.system.application.service.FormConfigTypeService;
import cn.cuiot.dmp.system.domain.aggregate.FormConfigType;
import cn.cuiot.dmp.system.domain.repository.FormConfigTypeRepository;
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
public class FormConfigTypeServiceImpl implements FormConfigTypeService {

    @Autowired
    private FormConfigTypeRepository formConfigTypeRepository;

    @Override
    public FormConfigTypeVO queryForDetail(Long id) {
        FormConfigType formConfigType = formConfigTypeRepository.queryForDetail(id);
        FormConfigTypeVO formConfigTypeVO = new FormConfigTypeVO();
        BeanUtils.copyProperties(formConfigType, formConfigTypeVO);
        return formConfigTypeVO;
    }
}
