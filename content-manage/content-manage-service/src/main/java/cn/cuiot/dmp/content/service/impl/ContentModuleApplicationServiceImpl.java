package cn.cuiot.dmp.content.service.impl;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.content.conver.ModuleApplicationConvert;
import cn.cuiot.dmp.content.dal.entity.ContentModuleApplication;
import cn.cuiot.dmp.content.dal.mapper.ContentModuleApplicationMapper;
import cn.cuiot.dmp.content.param.dto.ModuleApplicationCreateDto;
import cn.cuiot.dmp.content.param.dto.ModuleApplicationUpdateDto;
import cn.cuiot.dmp.content.param.query.ModuleApplicationPageQuery;
import cn.cuiot.dmp.content.service.ContentModuleApplicationService;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
        moduleApplication.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        return save(moduleApplication);
    }

    @Override
    public Boolean update(ModuleApplicationUpdateDto updateDto) {
        ContentModuleApplication moduleApplication = ModuleApplicationConvert.INSTANCE.convert(updateDto);
        moduleApplication.setId(updateDto.getId());
        return updateById(moduleApplication);
    }

    @Override
    public Boolean deleteById(Long id) {
        LambdaUpdateWrapper<ContentModuleApplication> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ContentModuleApplication::getId, id);
        updateWrapper.eq(ContentModuleApplication::getCompanyId, LoginInfoHolder.getCurrentOrgId());
        return remove(updateWrapper);
    }

    @Override
    public Boolean updateStatus(UpdateStatusParam updateStatusParam) {
        ContentModuleApplication moduleApplication = getById(updateStatusParam.getId());
        if (moduleApplication == null) {
            return false;
        }
        moduleApplication.setStatus(updateStatusParam.getStatus());
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

    private LambdaUpdateWrapper<ContentModuleApplication> buildCommonQueryWrapper(ModuleApplicationPageQuery pageQuery) {
        LambdaUpdateWrapper<ContentModuleApplication> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.like(StrUtil.isNotEmpty(pageQuery.getName()), ContentModuleApplication::getName, pageQuery.getName());
        queryWrapper.eq(ContentModuleApplication::getCompanyId, LoginInfoHolder.getCurrentOrgId());
        queryWrapper.eq(Objects.nonNull(pageQuery.getStatus()), ContentModuleApplication::getStatus, pageQuery.getStatus());
        queryWrapper.orderByAsc(ContentModuleApplication::getSort);
        queryWrapper.orderByDesc(ContentModuleApplication::getUpdateTime);
        return queryWrapper;
    }
}
