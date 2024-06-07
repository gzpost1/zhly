package cn.cuiot.dmp.content.service.impl;

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.content.conver.ModuleBannerConvert;
import cn.cuiot.dmp.content.dal.entity.ContentModuleBanner;
import cn.cuiot.dmp.content.dal.mapper.ContentModuleBannerMapper;
import cn.cuiot.dmp.content.param.dto.ModuleBannerCreateDto;
import cn.cuiot.dmp.content.param.dto.ModuleBannerUpdateDto;
import cn.cuiot.dmp.content.param.query.ModuleBannerPageQuery;
import cn.cuiot.dmp.content.service.ContentModuleBannerService;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/3 17:23
 */
@Service
public class ContentModuleBannerServiceImpl extends ServiceImpl<ContentModuleBannerMapper, ContentModuleBanner> implements ContentModuleBannerService {

    @Override
    public Boolean create(ModuleBannerCreateDto createDto) {
        ContentModuleBanner contentModuleBanner = ModuleBannerConvert.INSTANCE.convert(createDto);
        contentModuleBanner.setStatus(EntityConstants.ENABLED);
        contentModuleBanner.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        return save(contentModuleBanner);
    }

    @Override
    public Boolean update(ModuleBannerUpdateDto updateDto) {
        ContentModuleBanner contentModuleBanner = ModuleBannerConvert.INSTANCE.convert(updateDto);
        contentModuleBanner.setId(updateDto.getId());
        return updateById(contentModuleBanner);
    }

    @Override
    public Boolean deleteById(Long id) {
        LambdaUpdateWrapper<ContentModuleBanner> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ContentModuleBanner::getId, id);
        updateWrapper.eq(ContentModuleBanner::getCompanyId, LoginInfoHolder.getCurrentOrgId());
        return remove(updateWrapper);
    }

    @Override
    public Boolean updateStatus(UpdateStatusParam updateStatusParam) {
        ContentModuleBanner moduleBanner = getById(updateStatusParam.getId());
        if (moduleBanner == null) {
            return false;
        }
        moduleBanner.setStatus(updateStatusParam.getStatus());
        return updateById(moduleBanner);
    }

    @Override
    public IPage<ContentModuleBanner> queryForPage(ModuleBannerPageQuery pageQuery) {
        LambdaQueryWrapper<ContentModuleBanner> queryWrapper = buildCommonQueryWrapper(pageQuery);
        queryWrapper.orderByDesc(ContentModuleBanner::getCreateTime);
        return page(new Page<>(pageQuery.getPageNo(), pageQuery.getPageSize()), queryWrapper);
    }

    @Override
    public List<ContentModuleBanner> queryForList(ModuleBannerPageQuery pageQuery) {
        LambdaQueryWrapper<ContentModuleBanner> queryWrapper = buildCommonQueryWrapper(pageQuery);
        return list(queryWrapper);
    }

    @Override
    public Map<Long, List<ContentModuleBanner>> getByModuleIdsAndSort(List<Long> bannerIds) {
        LambdaQueryWrapper<ContentModuleBanner> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContentModuleBanner::getModuleId, bannerIds);
        queryWrapper.le(ContentModuleBanner::getEffectiveStartTime, new Date());
        queryWrapper.ge(ContentModuleBanner::getEffectiveEndTime, new Date());
        queryWrapper.eq(ContentModuleBanner::getStatus, EntityConstants.ENABLED);
        queryWrapper.orderByAsc(ContentModuleBanner::getEffectiveStartTime);
        List<ContentModuleBanner> moduleBannerList = list(queryWrapper);
        if (CollUtil.isNotEmpty(moduleBannerList)) {
            return moduleBannerList.stream().collect(Collectors.groupingBy(ContentModuleBanner::getModuleId, LinkedHashMap::new, Collectors.toList()));
        }
        return new HashMap<>();
    }

    private static LambdaQueryWrapper<ContentModuleBanner> buildCommonQueryWrapper(ModuleBannerPageQuery pageQuery) {
        LambdaQueryWrapper<ContentModuleBanner> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContentModuleBanner::getCompanyId, LoginInfoHolder.getCurrentOrgId());
        queryWrapper.eq(pageQuery.getModuleId() != null, ContentModuleBanner::getModuleId, pageQuery.getModuleId());
        queryWrapper.like(StrUtil.isNotEmpty(pageQuery.getName()), ContentModuleBanner::getName, pageQuery.getName());
        queryWrapper.eq(Objects.nonNull(pageQuery.getStatus()), ContentModuleBanner::getStatus, pageQuery.getStatus());
        if (EntityConstants.EXPIRE.equals(pageQuery.getEffectiveState())) {
            queryWrapper.lt(ContentModuleBanner::getEffectiveEndTime, new Date());
        } else if (EntityConstants.NOT_EFFECTIVE.equals(pageQuery.getEffectiveState())) {
            queryWrapper.gt(ContentModuleBanner::getEffectiveStartTime, new Date());
        } else if (EntityConstants.NORMAL.equals(pageQuery.getEffectiveState())) {
            queryWrapper.le(ContentModuleBanner::getEffectiveStartTime, new Date());
            queryWrapper.ge(ContentModuleBanner::getEffectiveEndTime, new Date());
        }
        return queryWrapper;
    }
}
