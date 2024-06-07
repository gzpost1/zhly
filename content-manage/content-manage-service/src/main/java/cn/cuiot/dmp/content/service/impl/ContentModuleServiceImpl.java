package cn.cuiot.dmp.content.service.impl;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.content.constant.ApplicationConfigConstants;
import cn.cuiot.dmp.content.constant.ContentConstants;
import cn.cuiot.dmp.content.dal.entity.ContentModule;
import cn.cuiot.dmp.content.dal.mapper.ContentModuleMapper;
import cn.cuiot.dmp.content.service.ContentModuleService;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/3 17:23
 */
@Service
public class ContentModuleServiceImpl implements ContentModuleService {

    @Autowired
    private ContentModuleMapper contentModuleMapper;

    @Override
    @Transactional
    public void initModule(Long orgId) {
        LambdaQueryWrapper<ContentModule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContentModule::getCompanyId, orgId);
        if (contentModuleMapper.selectCount(queryWrapper) > 0) {
            return;
        }
        // 初始化管理端首页模块
        ContentModule contentModule = new ContentModule().setCompanyId(orgId).setSystemModule(ApplicationConfigConstants.SystemModule.MANAGEMENT_HOME)
                .setModuleType(ApplicationConfigConstants.ModuleType.TOP_BANNER).setShowed(ContentConstants.ShowStatus.NOT_SHOW).setSort(1);
        contentModuleMapper.insert(contentModule);
        contentModule = new ContentModule().setCompanyId(orgId).setSystemModule(ApplicationConfigConstants.SystemModule.MANAGEMENT_HOME)
                .setModuleType(ApplicationConfigConstants.ModuleType.NOTICE).setShowed(ContentConstants.ShowStatus.NOT_SHOW).setSort(2);
        contentModuleMapper.insert(contentModule);
        for (int i = 3; i <= 20; i++) {
            contentModule = new ContentModule().setCompanyId(orgId).setSystemModule(ApplicationConfigConstants.SystemModule.MANAGEMENT_HOME)
                    .setModuleType(ApplicationConfigConstants.ModuleType.APPLICATION).setShowed(ContentConstants.ShowStatus.NOT_SHOW).setSort(i);
            contentModuleMapper.insert(contentModule);
        }
        // 初始化客户端首页模块
        contentModule = new ContentModule().setCompanyId(orgId).setSystemModule(ApplicationConfigConstants.SystemModule.CLIENT_HOME)
                .setModuleType(ApplicationConfigConstants.ModuleType.TOP_BANNER).setShowed(ContentConstants.ShowStatus.NOT_SHOW).setSort(1);
        contentModuleMapper.insert(contentModule);
        contentModule = new ContentModule().setCompanyId(orgId).setSystemModule(ApplicationConfigConstants.SystemModule.CLIENT_HOME)
                .setModuleType(ApplicationConfigConstants.ModuleType.NOTICE).setShowed(ContentConstants.ShowStatus.NOT_SHOW).setSort(2);
        contentModuleMapper.insert(contentModule);
        contentModule = new ContentModule().setCompanyId(orgId).setSystemModule(ApplicationConfigConstants.SystemModule.CLIENT_HOME)
                .setModuleType(ApplicationConfigConstants.ModuleType.APPLICATION).setShowed(ContentConstants.ShowStatus.NOT_SHOW).setSort(3);
        contentModuleMapper.insert(contentModule);
        contentModule = new ContentModule().setCompanyId(orgId).setSystemModule(ApplicationConfigConstants.SystemModule.CLIENT_HOME)
                .setModuleType(ApplicationConfigConstants.ModuleType.STEWARD_APPLICATION).setShowed(ContentConstants.ShowStatus.NOT_SHOW).setSort(4);
        contentModuleMapper.insert(contentModule);
        contentModule = new ContentModule().setCompanyId(orgId).setSystemModule(ApplicationConfigConstants.SystemModule.CLIENT_HOME)
                .setModuleType(ApplicationConfigConstants.ModuleType.MIDDLE_BANNER).setShowed(ContentConstants.ShowStatus.NOT_SHOW).setSort(5);
        contentModuleMapper.insert(contentModule);
        // 初始化客户端物业管理模块
        for (int i = 1; i <= 20; i++) {
            contentModule = new ContentModule().setCompanyId(orgId).setSystemModule(ApplicationConfigConstants.SystemModule.CLIENT_PROPERTY)
                    .setModuleType(ApplicationConfigConstants.ModuleType.APPLICATION).setShowed(ContentConstants.ShowStatus.NOT_SHOW).setSort(i);
            contentModuleMapper.insert(contentModule);
        }
    }

    @Override
    public List<ContentModule> queryForList(String orgId, String systemModule) {
        LambdaQueryWrapper<ContentModule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContentModule::getSystemModule, systemModule);
        queryWrapper.eq(ContentModule::getCompanyId, orgId);
        queryWrapper.orderByAsc(ContentModule::getSort);
        return contentModuleMapper.selectList(queryWrapper);
    }

    @Override
    public Boolean updateShow(UpdateStatusParam statusParam) {
        ContentModule contentModule = contentModuleMapper.selectById(statusParam.getId());
        if (contentModule == null || !contentModule.getCompanyId().equals(LoginInfoHolder.getCurrentOrgId())) {
            return false;
        }
        contentModule.setShowed(statusParam.getStatus());
        contentModuleMapper.updateById(contentModule);
        return true;
    }

    @Override
    public Boolean update(ContentModule contentModule) {
        ContentModule oldContentModule = contentModuleMapper.selectById(contentModule.getId());
        if (!oldContentModule.getCompanyId().equals(LoginInfoHolder.getCurrentOrgId())) {
            return false;
        }
        contentModuleMapper.updateById(contentModule);
        return true;
    }

    @Override
    public List<ContentModule> getAppHomeModule(String systemModule) {
        LambdaQueryWrapper<ContentModule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContentModule::getCompanyId, LoginInfoHolder.getCurrentOrgId());
        queryWrapper.eq(ContentModule::getSystemModule, systemModule);
        queryWrapper.eq(ContentModule::getShowed, ContentConstants.ShowStatus.SHOW);
        queryWrapper.orderByAsc(ContentModule::getSort);
        return contentModuleMapper.selectList(queryWrapper);
    }
}