package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.system.domain.aggregate.CodeArchives;
import cn.cuiot.dmp.system.domain.aggregate.CodeArchivesPageQuery;
import cn.cuiot.dmp.system.domain.repository.CodeArchivesRepository;
import cn.cuiot.dmp.system.infrastructure.entity.CodeArchivesEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.CodeArchivesMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/5/20
 */
@Slf4j
@Service
public class CodeArchivesRepositoryImpl implements CodeArchivesRepository {

    @Autowired
    private CodeArchivesMapper codeArchivesMapper;

    @Override
    public PageResult<CodeArchives> queryForPage(CodeArchivesPageQuery pageQuery) {
        LambdaQueryWrapper<CodeArchivesEntity> queryWrapper = new LambdaQueryWrapper<CodeArchivesEntity>()
                .eq(Objects.nonNull(pageQuery.getId()), CodeArchivesEntity::getId, pageQuery.getId())
                .eq(Objects.nonNull(pageQuery.getArchiveType()), CodeArchivesEntity::getArchiveType, pageQuery.getArchiveType())
                .eq(Objects.nonNull(pageQuery.getCodeType()), CodeArchivesEntity::getCodeType, pageQuery.getArchiveType())
                .eq(Objects.nonNull(pageQuery.getStatus()), CodeArchivesEntity::getStatus, pageQuery.getStatus())
                .ge(Objects.nonNull(pageQuery.getBeginTime()), CodeArchivesEntity::getCreateTime, pageQuery.getBeginTime())
                .le(Objects.nonNull(pageQuery.getEndTime()), CodeArchivesEntity::getCreateTime, pageQuery.getEndTime());
        IPage<CodeArchivesEntity> codeArchivesEntityPage = codeArchivesMapper.selectPage(
                new Page<>(pageQuery.getPageNo(), pageQuery.getPageSize()), queryWrapper);
        if (CollectionUtils.isEmpty(codeArchivesEntityPage.getRecords())) {
            return new PageResult<>();
        }
        return codeArchivesEntity2CodeArchives(codeArchivesEntityPage);
    }

    @Override
    public CodeArchives queryForDetail(Long id) {
        CodeArchivesEntity codeArchivesEntity = Optional.ofNullable(codeArchivesMapper.selectById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        CodeArchives codeArchives = new CodeArchives();
        BeanUtils.copyProperties(codeArchivesEntity, codeArchives);
        return codeArchives;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveCodeArchives(CodeArchives codeArchives) {
        AssertUtil.notNull(codeArchives.getGenerateNum(), "生成数量不能为空");
        AssertUtil.notNull(codeArchives.getCodeType(), "编码类型不能为空");
        AssertUtil.notNull(codeArchives.getCompanyId(), "企业ID不能为空");
        AssertUtil.notNull(codeArchives.getCreateUser(), "创建人ID不能为空");
        List<CodeArchivesEntity> codeArchivesEntityList = new ArrayList<>();
        for (int i = 0; i < codeArchives.getGenerateNum(); ++i) {
            CodeArchivesEntity codeArchivesEntity = new CodeArchivesEntity();
            codeArchivesEntity.setId(IdWorker.getId());
            codeArchivesEntity.setCompanyId(codeArchives.getCompanyId());
            codeArchivesEntity.setCodeType(codeArchives.getCodeType());
            codeArchivesEntity.setCreateUser(codeArchives.getCreateUser());
            codeArchivesEntityList.add(codeArchivesEntity);
        }
        return codeArchivesMapper.batchSaveCodeArchives(codeArchivesEntityList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCodeArchives(CodeArchives codeArchives) {
        CodeArchivesEntity codeArchivesEntity = Optional.ofNullable(codeArchivesMapper.selectById(codeArchives.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        // 目前只有描述可以修改
        if (StringUtils.isNotBlank(codeArchives.getArchiveDesc())) {
            codeArchivesEntity.setArchiveDesc(codeArchives.getArchiveDesc());
        }
        return codeArchivesMapper.updateById(codeArchivesEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCodeArchivesStatus(CodeArchives codeArchives) {
        AssertUtil.notNull(codeArchives.getStatus(), "状态不能为空");
        CodeArchivesEntity codeArchivesEntity = Optional.ofNullable(codeArchivesMapper.selectById(codeArchives.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        codeArchivesEntity.setStatus(codeArchives.getStatus());
        return codeArchivesMapper.updateById(codeArchivesEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCodeArchives(Long id) {
        return codeArchivesMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int associateCodeArchives(CodeArchives codeArchives) {
        AssertUtil.notNull(codeArchives.getArchiveId(), "档案ID不能为空");
        AssertUtil.notNull(codeArchives.getArchiveType(), "档案类型不能为空");
        CodeArchivesEntity codeArchivesEntity = Optional.ofNullable(codeArchivesMapper.selectById(codeArchives.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        codeArchivesEntity.setArchiveId(codeArchives.getArchiveId());
        codeArchivesEntity.setArchiveType(codeArchives.getArchiveType());
        return codeArchivesMapper.updateById(codeArchivesEntity);
    }

    private PageResult<CodeArchives> codeArchivesEntity2CodeArchives(IPage<CodeArchivesEntity> codeArchivesEntityPage) {
        PageResult<CodeArchives> codeArchivesPageResult = new PageResult<>();
        List<CodeArchives> codeArchivesList = codeArchivesEntityPage.getRecords().stream()
                .map(o -> {
                    CodeArchives codeArchives = new CodeArchives();
                    BeanUtils.copyProperties(o, codeArchives);
                    // 如果已经绑定上档案，则需要查找档案名称
                    if (Objects.nonNull(o.getArchiveId())) {
                        List<String> archiveNameList = codeArchivesMapper.queryArchiveNameById(o.getArchiveId());
                        if (CollectionUtils.isNotEmpty(archiveNameList)) {
                            codeArchives.setArchiveName(archiveNameList.get(0));
                        }
                    }
                    return codeArchives;
                })
                .collect(Collectors.toList());
        codeArchivesPageResult.setList(codeArchivesList);
        codeArchivesPageResult.setCurrentPage((int) codeArchivesEntityPage.getCurrent());
        codeArchivesPageResult.setPageSize((int) codeArchivesEntityPage.getSize());
        codeArchivesPageResult.setTotal(codeArchivesEntityPage.getTotal());
        return codeArchivesPageResult;
    }

}
