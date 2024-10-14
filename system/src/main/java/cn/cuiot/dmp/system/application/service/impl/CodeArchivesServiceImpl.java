package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.base.application.dto.ExcelDownloadDto;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.param.dto.CodeArchivesCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.CodeArchivesUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.CodeArchivesVO;
import cn.cuiot.dmp.system.application.param.vo.export.CodeArchiveExportVo;
import cn.cuiot.dmp.system.application.service.CodeArchivesService;
import cn.cuiot.dmp.system.domain.aggregate.CodeArchives;
import cn.cuiot.dmp.system.domain.aggregate.CodeArchivesPageQuery;
import cn.cuiot.dmp.system.domain.repository.CodeArchivesRepository;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private ExcelExportService excelExportService;

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
        return codeArchivesRepository.updateCodeArchivesStatus(codeArchives);
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

    @Override
    public void export(CodeArchivesPageQuery pageQuery) throws Exception {
        excelExportService.excelExport(ExcelDownloadDto.<CodeArchivesPageQuery>builder().loginInfo(LoginInfoHolder.getCurrentLoginInfo()).query(pageQuery).title("二维码档案").fileName("二维码档案导出"+ "("+ DateTimeUtil.dateToString(new Date(), "yyyyMMdd")+")").sheetName("二维码档案").build(),
                CodeArchiveExportVo.class, this::executePageQuery);
    }

    private IPage<CodeArchiveExportVo> executePageQuery(ExcelDownloadDto<CodeArchivesPageQuery> codeArchivesPageQueryExcelDownloadDto) {
        CodeArchivesPageQuery pageQuery = codeArchivesPageQueryExcelDownloadDto.getQuery();
        PageResult<CodeArchives> pageResult = codeArchivesRepository.queryForPage(pageQuery);
        List<CodeArchiveExportVo> exportDataList = new ArrayList<>();
        pageResult.getList().forEach(o -> {
            CodeArchiveExportVo codeArchiveExportVo = new CodeArchiveExportVo();
            BeanUtils.copyProperties(o, codeArchiveExportVo);
            exportDataList.add(codeArchiveExportVo);
        });
        Page<CodeArchiveExportVo> page = new Page<>(pageResult.getPageNo(), pageResult.getPageSize(), pageResult.getTotal());
        return page.setRecords(exportDataList);
    }
}
