package cn.cuiot.dmp.content.service.impl;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.syslog.LogContextHolder;
import cn.cuiot.dmp.base.infrastructure.syslog.OptTargetData;
import cn.cuiot.dmp.base.infrastructure.syslog.OptTargetInfo;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.content.conver.ModuleApplicationConvert;
import cn.cuiot.dmp.content.dal.entity.ContentModuleApplication;
import cn.cuiot.dmp.content.dal.mapper.ContentModuleApplicationMapper;
import cn.cuiot.dmp.content.param.dto.ModuleApplicationCreateDto;
import cn.cuiot.dmp.content.param.dto.ModuleApplicationUpdateDto;
import cn.cuiot.dmp.content.param.query.ModuleApplicationPageQuery;
import cn.cuiot.dmp.content.service.ContentModuleApplicationService;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/3 17:22
 */
@Service
public class ContentModuleApplicationServiceImpl extends ServiceImpl<ContentModuleApplicationMapper, ContentModuleApplication> implements ContentModuleApplicationService {
    @Override
    public Boolean create(ModuleApplicationCreateDto moduleBannerCreateDto) {
        ContentModuleApplication moduleApplication = ModuleApplicationConvert.INSTANCE.convert(moduleBannerCreateDto);
        moduleApplication.setStatus(EntityConstants.DISABLED);
        moduleApplication.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        boolean save = save(moduleApplication);
        //设置日志操作对象内容
        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name("图文管理")
                .targetDatas(Lists.newArrayList(new OptTargetData(moduleApplication.getName(), moduleApplication.getId().toString())))
                .build());
        return save;
    }

    @Override
    public Boolean update(ModuleApplicationUpdateDto updateDto) {
        ContentModuleApplication moduleApplication = ModuleApplicationConvert.INSTANCE.convert(updateDto);
        moduleApplication.setId(updateDto.getId());
        boolean b = updateById(moduleApplication);
        //设置日志操作对象内容
        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name("图文管理")
                .targetDatas(Lists.newArrayList(new OptTargetData(moduleApplication.getName(), moduleApplication.getId().toString())))
                .build());
        return b;
    }

    @Override
    public Boolean deleteById(Long id) {
        ContentModuleApplication moduleApplication = getById(id);
        if (moduleApplication == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        LambdaUpdateWrapper<ContentModuleApplication> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ContentModuleApplication::getId, id);
        updateWrapper.eq(ContentModuleApplication::getCompanyId, LoginInfoHolder.getCurrentOrgId());
        boolean remove = remove(updateWrapper);
        //设置日志操作对象内容
        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name("图文管理")
                .targetDatas(Lists.newArrayList(new OptTargetData(moduleApplication.getName(), moduleApplication.getId().toString())))
                .build());
        return remove;
    }

    @Override
    public Boolean updateStatus(UpdateStatusParam updateStatusParam) {
        ContentModuleApplication moduleApplication = getById(updateStatusParam.getId());
        if (moduleApplication == null) {
            return false;
        }
        moduleApplication.setStatus(updateStatusParam.getStatus());
        //设置日志操作对象内容
        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name("图文管理")
                .targetDatas(Lists.newArrayList(new OptTargetData(moduleApplication.getName(), moduleApplication.getId().toString())))
                .build());
        return updateById(moduleApplication);
    }

    @Override
    public IPage<ContentModuleApplication> queryForPage(ModuleApplicationPageQuery pageQuery) {
        LambdaUpdateWrapper<ContentModuleApplication> queryWrapper = buildCommonQueryWrapper(pageQuery);
        return page(new Page<>(pageQuery.getPageNo(), pageQuery.getPageSize()), queryWrapper);
    }

    @Override
    public List<ContentModuleApplication> queryForList(ModuleApplicationPageQuery pageQuery) {
        return list(buildCommonQueryWrapper(pageQuery));
    }

    @Override
    public Map<Long, List<ContentModuleApplication>> getByModuleIdsAndSort(List<Long> applicationIds) {
        LambdaQueryWrapper<ContentModuleApplication> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContentModuleApplication::getModuleId, applicationIds);
        queryWrapper.eq(ContentModuleApplication::getStatus, EntityConstants.ENABLED);
        queryWrapper.orderByAsc(ContentModuleApplication::getSort);
        List<ContentModuleApplication> applicationList = list(queryWrapper);
        if (CollUtil.isNotEmpty(applicationList)) {
            return applicationList.stream().collect(Collectors.groupingBy(ContentModuleApplication::getModuleId, LinkedHashMap::new, Collectors.toList()));
        }
        return new HashMap<>();
    }

    private LambdaUpdateWrapper<ContentModuleApplication> buildCommonQueryWrapper(ModuleApplicationPageQuery pageQuery) {
        LambdaUpdateWrapper<ContentModuleApplication> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.like(StrUtil.isNotEmpty(pageQuery.getName()), ContentModuleApplication::getName, pageQuery.getName());
        queryWrapper.eq(ContentModuleApplication::getCompanyId, LoginInfoHolder.getCurrentOrgId());
        queryWrapper.eq(Objects.nonNull(pageQuery.getModuleId()), ContentModuleApplication::getModuleId, pageQuery.getModuleId());
        queryWrapper.eq(Objects.nonNull(pageQuery.getStatus()), ContentModuleApplication::getStatus, pageQuery.getStatus());
        queryWrapper.orderByAsc(ContentModuleApplication::getSort);
        queryWrapper.orderByDesc(ContentModuleApplication::getUpdateTime);
        return queryWrapper;
    }
}
