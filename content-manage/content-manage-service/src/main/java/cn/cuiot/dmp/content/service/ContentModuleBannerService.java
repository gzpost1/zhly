package cn.cuiot.dmp.content.service;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.content.dal.entity.ContentModuleBanner;
import cn.cuiot.dmp.content.param.dto.ModuleBannerCreateDto;
import cn.cuiot.dmp.content.param.dto.ModuleBannerUpdateDto;
import cn.cuiot.dmp.content.param.query.ModuleBannerPageQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/3 17:23
 */
public interface ContentModuleBannerService extends IService<ContentModuleBanner> {

    /**
     * 创建banner
     * @param moduleBannerCreateDto
     * @return
     */
    Boolean create(ModuleBannerCreateDto moduleBannerCreateDto);

    /**
     * 更新banner
     * @param updateDto
     * @return
     */
    Boolean update(ModuleBannerUpdateDto updateDto);

    /**
     * 删除banner
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
    IPage<ContentModuleBanner> queryForPage(ModuleBannerPageQuery pageQuery);

    /**
     * 列表查询
     * @param pageQuery
     * @return
     */
    List<ContentModuleBanner> queryForList(ModuleBannerPageQuery pageQuery);

    /**
     *  根据模块id查询banner
     * @param bannerIds
     * @return
     */
    Map<Long, List<ContentModuleBanner>> getByModuleIdsAndSort(List<Long> bannerIds);
}
