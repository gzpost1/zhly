package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.application.param.dto.CodeArchivesCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.CodeArchivesUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.CodeArchivesVO;
import cn.cuiot.dmp.system.application.service.CodeArchivesService;
import cn.cuiot.dmp.system.domain.aggregate.CodeArchives;
import cn.cuiot.dmp.system.domain.aggregate.CodeArchivesPageQuery;
import cn.cuiot.dmp.system.domain.repository.CodeArchivesRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/5/20
 */
@Slf4j
@Service
public class CodeArchivesServiceImpl implements CodeArchivesService {

    @Autowired
    private CodeArchivesRepository codeArchivesRepository;

    @Override
    public PageResult<CodeArchivesVO> queryForPage(CodeArchivesPageQuery pageQuery) {
        PageResult<CodeArchives> codeArchivesPageResult = codeArchivesRepository.queryForPage(pageQuery);
        if (CollectionUtils.isEmpty(codeArchivesPageResult.getList())) {
            return new PageResult<>();
        }
        PageResult<CodeArchivesVO> codeArchivesVOPageResult = new PageResult<>();
        List<CodeArchivesVO> codeArchivesVOList = codeArchivesPageResult.getList().stream()
                .map(o -> {
                    CodeArchivesVO codeArchivesVO = new CodeArchivesVO();
                    BeanUtils.copyProperties(o, codeArchivesVO);
                    return codeArchivesVO;
                })
                .collect(Collectors.toList());
        BeanUtils.copyProperties(codeArchivesPageResult, codeArchivesVOPageResult);
        codeArchivesVOPageResult.setList(codeArchivesVOList);
        return codeArchivesVOPageResult;
    }

    @Override
    public CodeArchivesVO queryForDetail(Long id) {
        CodeArchives codeArchives = codeArchivesRepository.queryForDetail(id);
        CodeArchivesVO codeArchivesVO = new CodeArchivesVO();
        BeanUtils.copyProperties(codeArchives, codeArchivesVO);
        return codeArchivesVO;
    }

    @Override
    public int saveCodeArchives(CodeArchivesCreateDTO createDTO) {
        CodeArchives codeArchives = new CodeArchives();
        BeanUtils.copyProperties(createDTO, codeArchives);
        codeArchives.setCreateUser(createDTO.getUserId());
        return codeArchivesRepository.saveCodeArchives(codeArchives);
    }

    @Override
    public int updateCodeArchives(CodeArchivesUpdateDTO updateDTO) {
        CodeArchives codeArchives = new CodeArchives();
        BeanUtils.copyProperties(updateDTO, codeArchives);
        return codeArchivesRepository.updateCodeArchives(codeArchives);
    }

    @Override
    public int updateCodeArchivesStatus(UpdateStatusParam updateStatusParam) {
        CodeArchives codeArchives = new CodeArchives();
        BeanUtils.copyProperties(updateStatusParam, codeArchives);
        return codeArchivesRepository.updateCodeArchives(codeArchives);
    }

    @Override
    public int deleteCodeArchives(Long id) {
        return codeArchivesRepository.deleteCodeArchives(id);
    }

    @Override
    public int associateCodeArchives(CodeArchivesUpdateDTO updateDTO) {
        CodeArchives codeArchives = new CodeArchives();
        BeanUtils.copyProperties(updateDTO, codeArchives);
        return codeArchivesRepository.associateCodeArchives(codeArchives);
    }
}
