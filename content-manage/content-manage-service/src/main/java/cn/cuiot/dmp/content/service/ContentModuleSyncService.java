package cn.cuiot.dmp.content.service;

import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyRelationDTO;
import cn.cuiot.dmp.base.application.service.DataSyncService;
import cn.cuiot.dmp.content.dal.entity.ContentModule;
import cn.cuiot.dmp.content.dal.entity.ContentModuleApplication;
import cn.cuiot.dmp.content.dal.entity.ContentModuleBanner;
import cn.cuiot.dmp.content.dal.mapper.ContentModuleApplicationMapper;
import cn.cuiot.dmp.content.dal.mapper.ContentModuleBannerMapper;
import cn.cuiot.dmp.content.dal.mapper.ContentModuleMapper;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.base.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 同步企业审小程序配置
 *
 * @Author: zc
 * @Date: 2024-11-06
 */
@Slf4j
@Component
public class ContentModuleSyncService extends DataSyncService<ContentModule> {

    @Resource
    private ContentModuleMapper contentModuleMapper;
    @Resource
    private ContentModuleApplicationMapper contentModuleApplicationMapper;
    @Resource
    private ContentModuleBannerMapper contentModuleBannerMapper;

    @Override
    public List<ContentModule> fetchData(Long sourceCompanyId) {
        List<ContentModule> list = contentModuleMapper.selectList(
                new LambdaQueryWrapper<ContentModule>()
                        .eq(ContentModule::getCompanyId, sourceCompanyId));
        if (CollectionUtils.isEmpty(list)) {
            log.error("企业初始化【小程序配置】异常，模板企业不存在数据");
        }
        return list;
    }

    @Override
    public List<SyncCompanyRelationDTO<ContentModule>> preprocessData(List<ContentModule> data, Long targetCompanyId) {
        return data.stream().map(item -> {
            Long oldId = item.getId();
            ContentModule entity = new ContentModule();
            BeanUtils.copyProperties(item, entity);
            entity.setId(IdWorker.getId());
            entity.setCompanyId(targetCompanyId);
            return new SyncCompanyRelationDTO<>(entity, oldId);
        }).collect(Collectors.toList());
    }

    @Override
    public void saveData(List<SyncCompanyRelationDTO<ContentModule>> data, Long targetCompanyId) {
        data.forEach(item -> contentModuleMapper.insert(item.getEntity()));
    }

    @Override
    public void syncAssociatedData(List<SyncCompanyRelationDTO<ContentModule>> targetData, SyncCompanyDTO dto) {
        // 目标企业id
        Long targetCompanyId = dto.getTargetCompanyId();
        // 日期
        Date date = new Date();
        // 当前用户
        Long currentUserId = LoginInfoHolder.getCurrentUserId();
        // 跳转类型
        byte skipType = (byte) 2;

        targetData.forEach(item -> {
            Long oldModuleId = item.getOldId();
            Long newModuleId = item.getEntity().getId();

            List<ContentModuleApplication> moduleApplications = buildApplicationList(oldModuleId, newModuleId, targetCompanyId, date, currentUserId, skipType);
            List<ContentModuleBanner> moduleBanners = buildBannerList(oldModuleId, newModuleId, targetCompanyId, date, currentUserId, skipType);

            // 批量保存模块配置-应用
            if (CollectionUtils.isNotEmpty(moduleApplications)) {
                moduleApplications.forEach(contentModuleApplicationMapper::insert);
            }
            // 批量保存模块管理-banner模块
            if (CollectionUtils.isNotEmpty(moduleBanners)) {
                moduleBanners.forEach(contentModuleBannerMapper::insert);
            }
        });
    }

    @Override
    public void cleanSyncData(SyncCompanyDTO dto) {
        List<ContentModule> contentModules = contentModuleMapper.selectList(
                new LambdaQueryWrapper<ContentModule>()
                        .eq(ContentModule::getCompanyId, dto.getTargetCompanyId()));
        if (CollectionUtils.isNotEmpty(contentModules)) {
            List<Long> ids = contentModules.stream().map(ContentModule::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(ids)) {
                contentModuleApplicationMapper.batchDeleteByModuleIds(ids);
                contentModuleBannerMapper.batchDeleteByModuleIds(ids);
            }
            contentModuleMapper.batchDeleteByIds(ids);
        }
    }


    /**
     * 构造模块配置-应用
     */
    private List<ContentModuleApplication> buildApplicationList(Long oldModuleId, Long newModuleId,Long targetCompanyId,
                                                            Date date, Long currentUserId, Byte skipType) {
        List<ContentModuleApplication> moduleApplications = contentModuleApplicationMapper.selectList(
                new LambdaQueryWrapper<ContentModuleApplication>()
                        .eq(ContentModuleApplication::getModuleId, oldModuleId));
        if (CollectionUtils.isEmpty(moduleApplications)) {
            return null;
        }
        return moduleApplications.stream().map(application -> {
            ContentModuleApplication moduleApplicationEntity = new ContentModuleApplication();
            BeanUtils.copyProperties(application, moduleApplicationEntity);
            moduleApplicationEntity.setId(IdWorker.getId());
            moduleApplicationEntity.setCompanyId(targetCompanyId);
            moduleApplicationEntity.setModuleId(newModuleId);
            moduleApplicationEntity.setCreateTime(date);
            moduleApplicationEntity.setUpdateTime(date);
            moduleApplicationEntity.setCreateUser(currentUserId);
            moduleApplicationEntity.setUpdateUser(currentUserId);
            if (Objects.equal(application.getSkipType(), skipType)) {
                moduleApplicationEntity.setSourceId(null);
            }
            return moduleApplicationEntity;
        }).collect(Collectors.toList());
    }

    /**
     * 构造模块管理-banner模块
     */
    private List<ContentModuleBanner> buildBannerList(Long oldModuleId, Long newModuleId,Long targetCompanyId,
                                                  Date date, Long currentUserId, Byte skipType) {
        List<ContentModuleBanner> banners = contentModuleBannerMapper.selectList(
                new LambdaQueryWrapper<ContentModuleBanner>()
                        .eq(ContentModuleBanner::getModuleId, oldModuleId));
        if (CollectionUtils.isEmpty(banners)) {
            return null;
        }
        return banners.stream().map(banner -> {
            ContentModuleBanner bannerEntity = new ContentModuleBanner();
            BeanUtils.copyProperties(banner, bannerEntity);
            bannerEntity.setId(IdWorker.getId());
            bannerEntity.setCompanyId(targetCompanyId);
            bannerEntity.setModuleId(newModuleId);
            bannerEntity.setCreateTime(date);
            bannerEntity.setUpdateTime(date);
            bannerEntity.setCreateUser(currentUserId);
            bannerEntity.setUpdateUser(currentUserId);
            if (Objects.equal(banner.getSkipType(), skipType)) {
                bannerEntity.setSourceId(null);
            }
            return bannerEntity;
        }).collect(Collectors.toList());
    }
}
