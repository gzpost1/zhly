package cn.cuiot.dmp.content.service;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.content.dal.entity.ContentModuleApplication;
import cn.cuiot.dmp.content.dal.entity.ContentModuleBanner;
import cn.cuiot.dmp.content.param.dto.ModuleApplicationCreateDto;
import cn.cuiot.dmp.content.param.dto.ModuleApplicationUpdateDto;
import cn.cuiot.dmp.content.param.query.ModuleApplicationPageQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/3 17:22
 */
public interface ContentModuleApplicationService extends IService<ContentModuleApplication> {

    /**
     * 创建应用
     * @param moduleBannerCreateDto
     * @return
     */
    Boolean create(ModuleApplicationCreateDto moduleBannerCreateDto);

    /**
     * 更新应用
     * @param updateDto
     * @return
     */
    Boolean update(ModuleApplicationUpdateDto updateDto);

    /**
     *
     * @param id
     * @return
     */
    Boolean deleteById(Long id);

    /**
     * 更新状态
     * @param updateStatusParam
     * @return
     */
    Boolean updateStatus(UpdateStatusParam updateStatusParam);

    /**
     * 分页查询
     * @param pageQuery
     * @return
     */
    IPage<ContentModuleApplication> queryForPage(ModuleApplicationPageQuery pageQuery);

    /**
     * 列表查询
     * @param pageQuery
     * @return
     */
    List<ContentModuleApplication> queryForList(ModuleApplicationPageQuery pageQuery);

    /**
     * 获取对应系统模块下应用
     * @param applicationIds
     * @return
     */
    Map<Long, List<ContentModuleApplication>> getByModuleIdsAndSort(List<Long> applicationIds);
}
