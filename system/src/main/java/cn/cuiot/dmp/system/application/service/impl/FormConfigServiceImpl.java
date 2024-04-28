package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.system.application.param.vo.FormConfigVO;
import cn.cuiot.dmp.system.application.service.FormConfigService;
import cn.cuiot.dmp.system.domain.aggregate.FormConfig;
import cn.cuiot.dmp.system.domain.repository.FormConfigRepository;
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

}
