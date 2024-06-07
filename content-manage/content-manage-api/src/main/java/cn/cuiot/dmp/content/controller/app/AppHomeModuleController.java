package cn.cuiot.dmp.content.controller.app;//	模板

import cn.cuiot.dmp.base.application.annotation.ResolveExtData;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.content.constant.ApplicationConfigConstants;
import cn.cuiot.dmp.content.dal.entity.ContentModule;
import cn.cuiot.dmp.content.dal.entity.ContentModuleApplication;
import cn.cuiot.dmp.content.dal.entity.ContentModuleBanner;
import cn.cuiot.dmp.content.param.vo.AppHomeModuleVo;
import cn.cuiot.dmp.content.service.ContentModuleApplicationService;
import cn.cuiot.dmp.content.service.ContentModuleBannerService;
import cn.cuiot.dmp.content.service.ContentModuleService;
import cn.hutool.core.collection.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 小程序首页模块控制器
 *
 * @author hantingyao
 * @Description
 * @data 2024/6/6 14:13
 */

@RestController
@RequestMapping("/app/appHomeModule")
@ResolveExtData
public class AppHomeModuleController extends BaseController {

    @Autowired
    private ContentModuleService moduleService;
    @Autowired
    private ContentModuleBannerService moduleBannerService;
    @Autowired
    private ContentModuleApplicationService applicationService;

    /**
     * 获取首页模块
     *
     * @param systemModule 系统模块
     * @return
     */
    @GetMapping("/getAppHomeModule")
    public AppHomeModuleVo getAppHomeModule(@RequestParam("systemModule") String systemModule) {
        AppHomeModuleVo appHomeModuleVo = new AppHomeModuleVo();
        List<ContentModule> appHomeModules = moduleService.getAppHomeModule(systemModule);
        appHomeModuleVo.setModuleList(appHomeModules);
        if (CollUtil.isNotEmpty(appHomeModules)) {
            Map<String, List<ContentModule>> moduleTypMap = appHomeModules.stream().collect(Collectors.groupingBy(ContentModule::getModuleType));
            if (moduleTypMap.containsKey(ApplicationConfigConstants.ModuleType.TOP_BANNER)) {
                List<ContentModule> contentModules = moduleTypMap.get(ApplicationConfigConstants.ModuleType.TOP_BANNER);
                List<Long> bannerIds = contentModules.stream().map(ContentModule::getId).collect(Collectors.toList());
                Map<Long, List<ContentModuleBanner>> bannerMap = moduleBannerService.getByModuleIdsAndSort(bannerIds);
                appHomeModuleVo.setBannerMap(bannerMap);
            }
            if (moduleTypMap.containsKey(ApplicationConfigConstants.ModuleType.APPLICATION)) {
                List<ContentModule> contentModules = moduleTypMap.get(ApplicationConfigConstants.ModuleType.APPLICATION);
                List<Long> applicationIds = contentModules.stream().map(ContentModule::getId).collect(Collectors.toList());
                Map<Long, List<ContentModuleApplication>> applicationMap = applicationService.getByModuleIdsAndSort(applicationIds);
                appHomeModuleVo.setApplicationMap(applicationMap);
            }
            if (moduleTypMap.containsKey(ApplicationConfigConstants.ModuleType.MIDDLE_BANNER)) {
                List<ContentModule> contentModules = moduleTypMap.get(ApplicationConfigConstants.ModuleType.MIDDLE_BANNER);
                List<Long> bannerIds = contentModules.stream().map(ContentModule::getId).collect(Collectors.toList());
                Map<Long, List<ContentModuleBanner>> bannerMap = moduleBannerService.getByModuleIdsAndSort(bannerIds);
                appHomeModuleVo.getBannerMap().putAll(bannerMap);
            }
            if (moduleTypMap.containsKey(ApplicationConfigConstants.ModuleType.STEWARD_APPLICATION)) {
                List<ContentModule> contentModules = moduleTypMap.get(ApplicationConfigConstants.ModuleType.STEWARD_APPLICATION);
                List<Long> applicationIds = contentModules.stream().map(ContentModule::getId).collect(Collectors.toList());
                Map<Long, List<ContentModuleApplication>> applicationMap = applicationService.getByModuleIdsAndSort(applicationIds);
                appHomeModuleVo.getApplicationMap().putAll(applicationMap);
            }
        }
        return appHomeModuleVo;
    }
}
