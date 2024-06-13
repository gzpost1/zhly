package cn.cuiot.dmp.content.service;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.content.dal.entity.ContentModule;

import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/6/3 17:05
 */
public interface ContentModuleService {

    /**
     * 根据企业ID初始化模块
     *
     * @param orgId
     */
    void initModule(Long orgId);

    /**
     * 查询模块列表
     * @param orgId
     * @param systemModule
     * @return
     */
    List<ContentModule> queryForList(String orgId, String systemModule);

    /**
     * 更新显示状态
     * @param statusParam
     * @return
     */
    Boolean updateShow(UpdateStatusParam statusParam);

    /**
     *
     * @param contentModule
     * @return
     */
    Boolean update(ContentModule contentModule);

    /**
     * 获取小程序首页模块
     * @param orgId
     * @return
     */
    List<ContentModule> getAppHomeModule(String systemModule);
}
